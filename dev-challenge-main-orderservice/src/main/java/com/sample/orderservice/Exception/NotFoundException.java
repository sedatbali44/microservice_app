package com.sample.orderservice.Exception;

public class NotFoundException extends RuntimeException{

    private static final long serialVersionUID = -4471754942923414698L;

    public NotFoundException(String message) {
        super(message);
    }
}
