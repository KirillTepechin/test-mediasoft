package com.mediasoft.warehouse.exception;

import java.util.UUID;

public class CustomerNotFoundException extends RuntimeException{
    public CustomerNotFoundException(Long id){
        super(String.format("Пользователь с id %s не найден", id));
    }
}