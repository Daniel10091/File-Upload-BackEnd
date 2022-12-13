package com.example.upload.exception;

public class MaxSizeExceededException extends RuntimeException {

    public MaxSizeExceededException(String message) {
        super(message);
    }

}
