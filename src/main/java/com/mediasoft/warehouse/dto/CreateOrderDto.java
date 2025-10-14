package com.mediasoft.warehouse.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderDto {
    @NotBlank(message = "Адрес доставки не может быть пуст")
    private String deliveryAddress;
    @Valid
    private List<OrderProductDto> products;
}
