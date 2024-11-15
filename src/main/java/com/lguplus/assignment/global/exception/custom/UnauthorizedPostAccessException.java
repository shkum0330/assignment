package com.lguplus.assignment.global.exception.custom;

public class UnauthorizedPostAccessException extends RuntimeException{
    public UnauthorizedPostAccessException(String message) {
        super(message);
    }
}
