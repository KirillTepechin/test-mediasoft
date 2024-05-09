package com.mediasoft.warehouse.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class OrderProductDto {
    @NotNull(message = "uuid не может быть null")
    private UUID uuid;
    @NotNull(message = "Поле quantity не может быть null")
    @Positive(message = "Поле quantity должно быть положительным")
    private BigDecimal quantity;
}
