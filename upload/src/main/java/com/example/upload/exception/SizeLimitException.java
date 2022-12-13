package com.example.upload.exception;

public class SizeLimitException extends RuntimeException {

    public SizeLimitException(String message) {
        super(message);
    }

}
