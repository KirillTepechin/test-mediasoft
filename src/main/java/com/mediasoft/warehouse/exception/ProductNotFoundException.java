package com.mediasoft.warehouse.exception;

import java.util.UUID;

/**
 * Исключение, которое выбрасывается, когда товар с указанным UUID не найден
 */
public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(UUID uuid){
        super(String.format("Товар с артикулом %s не найден", uuid));
    }
}
