package com.icbt.ap.mobileaccessoriessales.exception;

import lombok.Getter;

@Getter
public class CustomAuthException extends RuntimeException{
    private final String code;
    private final String message;
    private final String[] args;

    public CustomAuthException(String code, String message, String[] args) {
        this.code = code;
        this.message = message;
        this.args = args;
    }
}
