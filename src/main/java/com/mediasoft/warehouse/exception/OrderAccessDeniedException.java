package com.mediasoft.warehouse.exception;

public class OrderAccessDeniedException extends RuntimeException{
    public OrderAccessDeniedException(){
        super("Доступ к заказу запрещен");
    }
}
