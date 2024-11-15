package com.lguplus.assignment.global.exception.custom;

public class NicknameAlreadyExistsException extends RuntimeException{
    public NicknameAlreadyExistsException(String message) {
        super(message);
    }
}
