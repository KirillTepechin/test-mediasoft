package com.mediasoft.warehouse.dto;

import com.mediasoft.warehouse.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderStatusDto {
    private OrderStatus status;
}
