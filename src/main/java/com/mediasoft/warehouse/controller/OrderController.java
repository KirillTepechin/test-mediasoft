package com.mediasoft.warehouse.controller;

import com.mediasoft.warehouse.dto.CreateOrderDto;
import com.mediasoft.warehouse.dto.GetOrderDto;
import com.mediasoft.warehouse.dto.OrderProductDto;
import com.mediasoft.warehouse.dto.OrderStatusDto;
import com.mediasoft.warehouse.model.enums.OrderStatus;
import com.mediasoft.warehouse.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public UUID createOrder(@RequestBody @Valid CreateOrderDto createOrderDto,
                              @RequestHeader("customer-id") Long customerId){
        return orderService.createOrder(customerId, createOrderDto);
    }

    @PatchMapping("/{orderUuid}")
    public UUID addProductsToOrder(@RequestBody @Valid List<OrderProductDto> products,
                                  @PathVariable UUID orderUuid,
                                  @RequestHeader("customer-id") Long customerId){
        return orderService.addProductsToOrder(products, orderUuid, customerId);
    }

    @GetMapping("/{orderUuid}")
    public GetOrderDto getOrderByUuid(@PathVariable UUID orderUuid,
                                      @RequestHeader("customer-id") Long customerId){
        return orderService.getOrderByUuid(orderUuid, customerId);
    }

    @DeleteMapping("/{orderUuid}")
    public void deleteOrder(@PathVariable UUID orderUuid,
                            @RequestHeader("customer-id") Long customerId){
        orderService.deleteOrder(orderUuid, customerId);
    }

    @PostMapping("/{orderUuid}/confirm")
    public void confirmOrder(@PathVariable UUID orderUuid){
        orderService.confirmOrder(orderUuid);
    }

    @PatchMapping("/{orderUuid}/status")
    public OrderStatus changeOrderStatus(@PathVariable UUID orderUuid,
                                  @RequestBody OrderStatusDto statusDto){
        return orderService.changeOrderStatus(orderUuid, statusDto);
    }
}
