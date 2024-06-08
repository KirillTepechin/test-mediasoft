package com.mediasoft.warehouse.dto;

import com.mediasoft.warehouse.model.enums.Category;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Data
public class UpdateProductDto {
    /**
     * Название товара.
     */
    @Length(min = 3, max = 255,
            message = "Поле name должно иметь длину от 3 до 255")
    private String name;
    /**
     * Артикул товара.
     */
    @Length(min = 3, max = 255,
            message = "Поле article должно иметь длину от 3 до 255")
    private String article;
    /**
     * Описание товара.
     */
    @Length(min = 3, max = 1000,
            message = "Поле description должно иметь длину от 3 до 1000")
    private String description;
    /**
     * Категория {@link Category} товара.
     */
    private Category category;
    /**
     * Цена товара.
     */
    @Positive(message = "Поле price должно быть положительным")
    private BigDecimal price;
    /**
     * Количество товара.
     */
    @Positive(message = "Поле quantity должно быть положительным")
    private BigDecimal quantity;

    private Boolean isAvailable;
}
