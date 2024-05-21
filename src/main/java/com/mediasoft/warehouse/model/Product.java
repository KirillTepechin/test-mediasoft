package com.mediasoft.warehouse.model;

import com.mediasoft.warehouse.model.enums.Category;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность товара в базе данных.
 */
@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Product {

    /**
     * Уникальный идентификатор товара
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false, unique = true)
    @NonNull
    private String article;

    /**
     * Название товара.
     */
    @Column(nullable = false)
    @NonNull
    private String name;

    /**
     * Описание товара.
     */
    @Column(nullable = false, length = 1000)
    @NonNull
    private String description;

    /**
     * Категория {@link Category} товара.
     */
    @Column(nullable = false)
    @NonNull
    private Category category;

    /**
     * Цена товара.
     */
    @Column(nullable = false)
    @NonNull
    private BigDecimal price;

    /**
     * Количество товара.
     */
    @Column(nullable = false)
    @NonNull
    private BigDecimal quantity;

    /**
     * Дата последнего изменения количества.
     */
    @Column(nullable = false)
    private LocalDateTime lastQuantityChangeDate;

    /**
     * Дата создания.
     */
    @Column(nullable = false, updatable = false)
    private LocalDate createdDate;

    @Column(nullable = false)
    private Boolean isAvailable;

    /**
     * Устанавливает дату создания и последнего изменения количества товара перед сохранением.
     */
    @PrePersist
    void setDates(){
        this.createdDate = LocalDate.now();
        this.lastQuantityChangeDate = LocalDateTime.now();
    }
}
