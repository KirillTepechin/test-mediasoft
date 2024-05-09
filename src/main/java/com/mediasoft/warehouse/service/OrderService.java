package com.mediasoft.warehouse.service;

import com.mediasoft.warehouse.dto.CreateOrderDto;
import com.mediasoft.warehouse.dto.GetOrderDto;
import com.mediasoft.warehouse.dto.GetOrderProductDto;
import com.mediasoft.warehouse.dto.OrderProductDto;
import com.mediasoft.warehouse.dto.OrderStatusDto;
import com.mediasoft.warehouse.exception.CustomerNotFoundException;
import com.mediasoft.warehouse.exception.InsufficientQuantityException;
import com.mediasoft.warehouse.exception.OrderAccessDeniedException;
import com.mediasoft.warehouse.exception.OrderNotFoundException;
import com.mediasoft.warehouse.exception.OrderUnsupportedStatusException;
import com.mediasoft.warehouse.exception.ProductNotAvailableException;
import com.mediasoft.warehouse.exception.ProductNotFoundException;
import com.mediasoft.warehouse.mapper.OrderMapper;
import com.mediasoft.warehouse.model.Customer;
import com.mediasoft.warehouse.model.Order;
import com.mediasoft.warehouse.model.OrderProduct;
import com.mediasoft.warehouse.model.OrderProductId;
import com.mediasoft.warehouse.model.Product;
import com.mediasoft.warehouse.model.enums.OrderStatus;
import com.mediasoft.warehouse.repository.CustomerRepository;
import com.mediasoft.warehouse.repository.OrderProductRepository;
import com.mediasoft.warehouse.repository.OrderRepository;
import com.mediasoft.warehouse.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;

    @Transactional
    public UUID createOrder(Long customerId, CreateOrderDto createOrderDto) {
        Customer currentCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        Order order = orderRepository.save(orderMapper
                .toOrder(createOrderDto.getDeliveryAddress(), currentCustomer));

        List<Product> productsFromDb = productRepository.findAllById(createOrderDto.getProducts().stream()
                .map(OrderProductDto::getUuid).toList());

        sumAndRemoveDuplicates(createOrderDto.getProducts()).forEach(product -> {
            Product productFromDb = productsFromDb.stream()
                    .filter(p -> p.getUuid().equals(product.getUuid()))
                    .findFirst()
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

        List<OrderProduct> orderProductsFromDb = orderProductRepository.findAllById(products.stream()
                .map(product -> new OrderProductId(orderUuid, product.getUuid())).toList());

        sumAndRemoveDuplicates(products).forEach(product -> {
            Product productFromDb = productsFromDb.stream()
                    .filter(p -> p.getUuid().equals(product.getUuid()))
                    .findFirst()
                    .orElseThrow(() -> new ProductNotFoundException(product.getUuid()));

            orderProductsFromDb.stream()
                    .filter(op -> op.getId().getOrderUuid().equals(orderUuid) &&
                            op.getId().getProductUuid().equals(productFromDb.getUuid()))
                    .findFirst()
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

        List<GetOrderProductDto> orderProducts = orderRepository.findOrderProductsByOrderUuid(orderUuid);
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

        List<OrderProduct> orderProducts = orderProductRepository.findAllByOrderUuid(orderUuid);
        List<Product> products = productRepository.findAllById(orderProducts.stream()
                .map(orderProduct -> orderProduct.getId().getProductUuid()).toList());

        orderProducts.forEach(orderProduct -> {
            Product product = products.stream()
                    .filter(p -> p.getUuid().equals(orderProduct.getId().getProductUuid()))
                    .findFirst()
                    .orElseThrow(() -> new ProductNotFoundException(orderProduct.getId().getProductUuid()));
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

    private void createOrderProduct(OrderProductDto product, Product productFromDb, Order order) {
        checkAddingPossibility(product, productFromDb);

        OrderProductId orderProductId = new OrderProductId(order.getUuid(), productFromDb.getUuid());
        OrderProduct orderProduct = new OrderProduct(orderProductId,
                product.getQuantity(), productFromDb.getPrice().multiply(product.getQuantity()));
        orderProductRepository.save(orderProduct);
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
