package com.mediasoft.warehouse.exception;

import java.util.UUID;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(UUID uuid){
        super(String.format("Заказ с uuid %s не найден", uuid));
    }
}
