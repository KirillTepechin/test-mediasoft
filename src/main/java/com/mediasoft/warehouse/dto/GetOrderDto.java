package com.mediasoft.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetOrderDto {
    private UUID orderUuid;
    private List<GetOrderProductDto> products;
    private BigDecimal totalPrice;
}
