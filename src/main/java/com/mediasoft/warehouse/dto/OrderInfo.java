package com.mediasoft.warehouse.dto;

import com.mediasoft.warehouse.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class OrderInfo {
    private UUID uuid;
    private CustomerInfo customer;
    private OrderStatus status;
    private String deliveryAddress;
    private BigDecimal quantity;
}
