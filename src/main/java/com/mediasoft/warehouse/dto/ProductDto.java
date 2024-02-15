package com.mediasoft.warehouse.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mediasoft.warehouse.model.Category;
import com.mediasoft.warehouse.model.Product;
import com.mediasoft.warehouse.validation.ValidationMarkers;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * DTO для {@link Product}.
 */
@Getter
@Setter
public class ProductDto {
    /**
     * Уникальный идентификатор товара (артикул)
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID uuid;

    /**
     * Название товара.
     */
    @NotBlank(groups = {ValidationMarkers.PostStrategy.class}, message = "Поле name не может быть null или пусто")
    @Length(min = 3, max = 255,
            groups = {ValidationMarkers.PostStrategy.class, ValidationMarkers.PatchStrategy.class},
            message = "Поле name должно иметь длину от 3 до 255")
    private String name;

    /**
     * Описание товара.
     */
    @NotBlank(groups = {ValidationMarkers.PostStrategy.class},
            message = "Поле description не может быть null или пусто")
    @Length(min = 3, max = 1000,
            groups = {ValidationMarkers.PostStrategy.class, ValidationMarkers.PatchStrategy.class},
            message = "Поле description должно иметь длину от 3 до 1000")
    private String description;

    /**
     * Категория {@link Category} товара.
     */
    @NotNull(groups = {ValidationMarkers.PostStrategy.class}, message = "Поле category не может быть null")
    private Category category;

    /**
     * Цена товара.
     */
    @NotNull(groups = {ValidationMarkers.PostStrategy.class}, message = "Поле price не может быть null")
    @Positive(groups = {ValidationMarkers.PostStrategy.class, ValidationMarkers.PatchStrategy.class},
            message = "Поле price должно быть положительным")
    private BigDecimal price;

    /**
     * Количество товара.
     */
    @NotNull(groups = {ValidationMarkers.PostStrategy.class}, message = "Поле price не может быть null")
    @Positive(groups = {ValidationMarkers.PostStrategy.class, ValidationMarkers.PatchStrategy.class},
            message = "Поле quantity должно быть положительным")
    private Integer quantity;

    /**
     * Дата последнего изменения количества.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime lastQuantityChangeDate;

    /**
     * Дата создания.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime createdDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDto that = (ProductDto) o;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name, description, category, price, quantity, lastQuantityChangeDate, createdDate);
    }
}
