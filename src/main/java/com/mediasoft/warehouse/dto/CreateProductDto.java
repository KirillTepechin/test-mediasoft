package com.mediasoft.warehouse.dto;

import com.mediasoft.warehouse.model.enums.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Data
public class CreateProductDto {
    /**
     * Название товара.
     */
    @NotBlank(message = "Поле name не может быть null или пусто")
    @Length(min = 3, max = 255,
            message = "Поле name должно иметь длину от 3 до 255")
    private String name;
    /**
     * Артикул товара.
     */
    @NotBlank(message = "Поле article не может быть null или пусто")
    @Length(min = 3, max = 255,
            message = "Поле article должно иметь длину от 3 до 255")
    private String article;
    /**
     * Описание товара.
     */
    @NotBlank(message = "Поле description не может быть null или пусто")
    @Length(min = 3, max = 1000,
            message = "Поле description должно иметь длину от 3 до 1000")
    private String description;
    /**
     * Категория {@link Category} товара.
     */
    @NotNull(message = "Поле category не может быть null")
    private Category category;
    /**
     * Цена товара.
     */
    @NotNull(message = "Поле price не может быть null")
    @Positive(message = "Поле price должно быть положительным")
    private BigDecimal price;
    /**
     * Количество товара.
     */
    @NotNull(message = "Поле price не может быть null")
    @Positive(message = "Поле quantity должно быть положительным")
    private BigDecimal quantity;

    @NotNull(message = "Поле is_available не может быть null")
    private Boolean isAvailable;
}
