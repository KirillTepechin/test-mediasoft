package com.mediasoft.warehouse.exception;

import java.util.UUID;

public class InsufficientQuantityException extends RuntimeException{
    public InsufficientQuantityException(){
        super("Недостаточно количества товара на складе");
    }
}