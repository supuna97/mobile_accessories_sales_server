package com.icbt.ap.mobileaccessoriessales.exception;

import lombok.Getter;

@Getter
public class CustomServiceException extends RuntimeException{
    private final String code;
    private final String message;
    private final Throwable throwable;
    private final String[] args;

    public CustomServiceException(String code, String message) {
        this.code = code;
        this.message = message;
        this.throwable = null;
        this.args = new String[0];
    }

    public CustomServiceException(String code, String message, String[] args) {
        this.code = code;
        this.message = message;
        this.args = args;
        this.throwable = null;
    }

    public CustomServiceException(String code, String message, Throwable throwable) {
        this.code = code;
        this.message = message;
        this.throwable = throwable;
        this.args = new String[0];
    }

    public CustomServiceException(String code, String message, Throwable throwable, String[] args) {
        this.code = code;
        this.message = message;
        this.throwable = throwable;
        this.args = args;
    }
}
