package com.mediasoft.warehouse.exception;

public class OrderUnsupportedStatusException extends RuntimeException{
    public OrderUnsupportedStatusException(){
        super("Ошибка статуса заказа");
    }
}
