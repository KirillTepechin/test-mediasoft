package com.mediasoft.warehouse.dto;

import com.mediasoft.warehouse.model.enums.OrderStatus;
import lombok.Data;

@Data
public class OrderStatusDto {
    private OrderStatus status;
}
