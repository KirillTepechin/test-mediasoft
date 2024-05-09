package com.mediasoft.warehouse.dto;

import com.mediasoft.warehouse.model.enums.Category;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class GetProductDto {
    /**
     * Уникальный идентификатор товара (артикул)
     */
    private UUID uuid;
    /**
     * Название товара.
     */
    private String name;
    /**
     * Артикул товара.
     */
    private String article;
    /**
     * Описание товара.
     */
    private String description;
    /**
     * Категория {@link Category} товара.
     */
    private Category category;
    /**
     * Цена товара.
     */
    private BigDecimal price;
    /**
     * Количество товара.
     */
    private BigDecimal quantity;
    /**
     * Дата последнего изменения количества.
     */
    private LocalDateTime lastQuantityChangeDate;
    /**
     * Дата создания.
     */
    private LocalDate createdDate;
}
