package com.mediasoft.warehouse.validation;

/**
 * Интерфейс для обозначения маркеров валидации.
 */
public interface ValidationMarkers {
    /**
     * Маркер для стратегии валидации при создании сущности.
     */
    interface PostStrategy {};
    /**
     * Маркер для стратегии валидации при обновлении сущности.
     */
    interface PatchStrategy {};
}
