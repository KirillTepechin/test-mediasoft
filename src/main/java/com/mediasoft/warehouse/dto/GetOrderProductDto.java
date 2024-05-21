package com.mediasoft.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetOrderProductDto {
    private UUID productId;
    private String name;
    private BigDecimal quantity;
    private BigDecimal price;
}
