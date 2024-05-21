package com.mediasoft.warehouse.exception;

public class ProductNotAvailableException extends RuntimeException{
    public ProductNotAvailableException(){
        super("Товар недоступен для заказа");
    }
}
