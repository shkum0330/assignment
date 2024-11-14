package com.lguplus.assignment.global.exception;

public class UnauthorizedPostAccessException extends RuntimeException{
    public UnauthorizedPostAccessException(String message) {
        super(message);
    }
}
