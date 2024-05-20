package com.mediasoft.warehouse.service;

import com.mediasoft.warehouse.dto.CreateOrderDto;
import com.mediasoft.warehouse.dto.CustomerInfo;
import com.mediasoft.warehouse.dto.GetOrderDto;
import com.mediasoft.warehouse.dto.GetOrderProductDto;
import com.mediasoft.warehouse.dto.OrderInfo;
import com.mediasoft.warehouse.dto.OrderProductDto;
import com.mediasoft.warehouse.dto.OrderStatusDto;
import com.mediasoft.warehouse.exception.CustomerNotFoundException;
import com.mediasoft.warehouse.exception.InsufficientQuantityException;
import com.mediasoft.warehouse.exception.OrderAccessDeniedException;
import com.mediasoft.warehouse.exception.OrderNotFoundException;
import com.mediasoft.warehouse.exception.OrderUnsupportedStatusException;
import com.mediasoft.warehouse.exception.ProductNotAvailableException;
import com.mediasoft.warehouse.exception.ProductNotFoundException;
import com.mediasoft.warehouse.integration.account.AccountServiceClient;
import com.mediasoft.warehouse.integration.crm.CrmServiceClient;
import com.mediasoft.warehouse.mapper.OrderMapper;
import com.mediasoft.warehouse.model.Customer;
import com.mediasoft.warehouse.model.Order;
import com.mediasoft.warehouse.model.OrderProduct;
import com.mediasoft.warehouse.model.OrderProductId;
import com.mediasoft.warehouse.model.Product;
import com.mediasoft.warehouse.model.enums.OrderStatus;
import com.mediasoft.warehouse.repository.CustomerRepository;
import com.mediasoft.warehouse.repository.OrderRepository;
import com.mediasoft.warehouse.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final AccountServiceClient accountServiceClient;
    private final CrmServiceClient crmServiceClient;

    @Transactional
    public UUID createOrder(Long customerId, CreateOrderDto createOrderDto) {
        Customer currentCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        Order order = orderRepository.save(orderMapper
                .toOrder(createOrderDto.getDeliveryAddress(), currentCustomer, new ArrayList<>()));

        List<Product> productsFromDb = productRepository.findAllById(createOrderDto.getProducts().stream()
                .map(OrderProductDto::getUuid).toList());

        final Map<UUID, Product> productMap = productsFromDb.stream()
                .collect(Collectors.toMap(Product::getUuid, Function.identity()));

        sumAndRemoveDuplicates(createOrderDto.getProducts()).forEach(product -> {
            final Product productFromDb =  Optional.of(productMap.get(product.getUuid()))
                    .orElseThrow(() -> new ProductNotFoundException(product.getUuid()));

            createOrderProduct(product, productFromDb, order);
        });

        return order.getUuid();
    }

    @Transactional
    public UUID addProductsToOrder(List<OrderProductDto> products, UUID orderUuid, Long customerId) {
        Order orderFromDb = orderRepository.findById(orderUuid)
                .orElseThrow(() -> new OrderNotFoundException(orderUuid));

        if (!Objects.equals(orderFromDb.getCustomer().getId(), customerId)) {
            throw new OrderAccessDeniedException();
        }
        if (!OrderStatus.CREATED.equals(orderFromDb.getStatus())) {
            throw new OrderUnsupportedStatusException();
        }

        List<Product> productsFromDb = productRepository.findAllById(products.stream()
                .map(OrderProductDto::getUuid).toList());

        final Map<UUID, Product> productMap = productsFromDb.stream()
                .collect(Collectors.toMap(Product::getUuid, Function.identity()));

        final Map<OrderProductId, OrderProduct> orderProductsMap = orderFromDb.getOrderProducts()
                .stream().collect(Collectors.toMap(OrderProduct::getId, Function.identity()));

        sumAndRemoveDuplicates(products).forEach(product -> {
            final Product productFromDb =  Optional.of(productMap.get(product.getUuid()))
                    .orElseThrow(() -> new ProductNotFoundException(product.getUuid()));

            Optional.ofNullable(orderProductsMap.get(new OrderProductId(orderUuid, product.getUuid())))
                    .ifPresentOrElse(op -> updateOrderProduct(product, productFromDb, op),
                            () -> createOrderProduct(product, productFromDb, orderFromDb));
        });

        return orderUuid;
    }
    @Transactional(readOnly = true)
    public GetOrderDto getOrderByUuid(UUID orderUuid, Long customerId){
        Order orderFromDb = orderRepository.findById(orderUuid)
                .orElseThrow(() -> new OrderNotFoundException(orderUuid));

        if (!Objects.equals(orderFromDb.getCustomer().getId(), customerId)) {
            throw new OrderAccessDeniedException();
        }

        List<GetOrderProductDto> orderProducts = orderRepository.findOrderProductsByOrderId(orderUuid);
        BigDecimal totalPrice = orderProducts.stream()
                .map(GetOrderProductDto::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        return new GetOrderDto(orderUuid, orderProducts, totalPrice);
    }
    @Transactional
    public void deleteOrder(UUID orderUuid, Long customerId) {
        Order orderFromDb = orderRepository.findById(orderUuid)
                .orElseThrow(() -> new OrderNotFoundException(orderUuid));

        if (!Objects.equals(orderFromDb.getCustomer().getId(), customerId)) {
            throw new OrderAccessDeniedException();
        }
        if (OrderStatus.CREATED.equals(orderFromDb.getStatus())) {
            orderFromDb.setStatus(OrderStatus.CANCELLED);
        }
        else {
            throw new OrderUnsupportedStatusException();
        }

        List<OrderProduct> orderProducts = orderFromDb.getOrderProducts();

        Map<UUID, Product> productMap = orderProducts.stream().map(OrderProduct::getProduct)
                .collect(Collectors.toMap(Product::getUuid, Function.identity()));

        orderProducts.forEach(orderProduct -> {
            final Product product =  Optional.of(productMap.get(orderProduct.getId().getProductId()))
                    .orElseThrow(() -> new ProductNotFoundException(orderProduct.getId().getProductId()));
            product.setQuantity(product.getQuantity().add(orderProduct.getQuantity()));
        });
    }

    @Transactional
    public void confirmOrder(UUID orderUuid) {
        //TODO
    }
    @Transactional
    public OrderStatus changeOrderStatus(UUID orderUuid, OrderStatusDto statusDto) {
        Order orderFromDb = orderRepository.findById(orderUuid)
                .orElseThrow(() -> new OrderNotFoundException(orderUuid));
        orderFromDb.setStatus(statusDto.getStatus());
        return orderFromDb.getStatus();
    }

    @Transactional(readOnly = true)
    public Map<UUID, List<OrderInfo>> getActiveOrdersInfo(){
        List<Order> activeOrders = orderRepository.findAllByStatusIn(
                List.of(OrderStatus.CREATED, OrderStatus.CONFIRMED)
        );

        String[] logins = activeOrders.stream().map(order -> order.getCustomer().getLogin())
                .toArray(String[]::new);

        CompletableFuture<Map<String, String>> accountNumbersByLoginsFuture = accountServiceClient.getAccountNumbersByLogins(logins);

        CompletableFuture<Map<String, String>> innByLoginsFuture = crmServiceClient.getInnByLogins(logins);

        Map<String, String> accountNumbersByLogins = accountNumbersByLoginsFuture.join();
        Map<String, String> innByLogins = innByLoginsFuture.join();

        List<OrderProduct> orderProducts = activeOrders.stream()
                .flatMap(order -> order.getOrderProducts().stream()).toList();

        return orderProducts.stream()
                .collect(Collectors.groupingBy(
                        orderProduct -> orderProduct.getProduct().getUuid(),
                        Collectors.mapping(orderProduct -> {
                            Order order = orderProduct.getOrder();
                            Customer customer = order.getCustomer();
                            return new OrderInfo(order.getUuid(),
                                    new CustomerInfo(customer.getId(),
                                            accountNumbersByLogins.get(customer.getLogin()),
                                            customer.getEmail(),
                                            innByLogins.get(customer.getLogin())
                                    ),
                                    order.getStatus(),
                                    order.getDeliveryAddress(),
                                    orderProduct.getQuantity()
                            );
                        }, Collectors.toList())
                ));

    }

    private void createOrderProduct(OrderProductDto product, Product productFromDb, Order order) {
        checkAddingPossibility(product, productFromDb);

        OrderProductId orderProductId = new OrderProductId(order.getUuid(), productFromDb.getUuid());
        OrderProduct orderProduct = new OrderProduct(orderProductId, order, productFromDb,
                product.getQuantity(),productFromDb.getPrice().multiply(product.getQuantity()));
        order.getOrderProducts().add(orderProduct);
        productFromDb.setQuantity(productFromDb.getQuantity().subtract(product.getQuantity()));
    }

    private void updateOrderProduct(OrderProductDto product, Product productFromDb, OrderProduct orderProduct) {
        checkAddingPossibility(product, productFromDb);

        orderProduct.setQuantity(orderProduct.getQuantity().add(product.getQuantity()));
        orderProduct.setPrice(productFromDb.getPrice().multiply(orderProduct.getQuantity()));
        productFromDb.setQuantity(productFromDb.getQuantity().subtract(product.getQuantity()));
    }

    private void checkAddingPossibility(OrderProductDto product, Product productFromDb) {
        if (product.getQuantity().compareTo(productFromDb.getQuantity()) > 0) {
            throw new InsufficientQuantityException();
        }
        if (!productFromDb.getIsAvailable()) {
            throw new ProductNotAvailableException();
        }
    }

    private List<OrderProductDto> sumAndRemoveDuplicates(List<OrderProductDto> products) {
        return products.stream()
                .collect(Collectors.groupingBy(OrderProductDto::getUuid,
                        Collectors.reducing(BigDecimal.ZERO, OrderProductDto::getQuantity, BigDecimal::add)))
                .entrySet().stream()
                .map(entry -> new OrderProductDto(entry.getKey(), entry.getValue()))
                .toList();
    }
}
