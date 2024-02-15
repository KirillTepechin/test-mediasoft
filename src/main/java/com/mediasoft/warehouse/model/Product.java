package com.mediasoft.warehouse.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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
     * Уникальный идентификатор товара (артикул)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

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
    private Integer quantity;

    /**
     * Дата последнего изменения количества.
     */
    @Column(nullable = false)
    private LocalDateTime lastQuantityChangeDate;

    /**
     * Дата создания.
     */
    @Column(nullable = false)
    private LocalDateTime createdDate;

    /**
     * Устанавливает дату создания и последнего изменения количества товара перед сохранением.
     */
    @PrePersist
    void setDates(){
        this.createdDate = LocalDateTime.now();
        this.lastQuantityChangeDate = LocalDateTime.now();
    }
}
