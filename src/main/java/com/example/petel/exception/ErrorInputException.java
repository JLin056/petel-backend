package com.example.petel.exception;

/**
 * 欄位輸入異常
 */
public class ErrorInputException extends RuntimeException {

    public ErrorInputException() {
    }

    public ErrorInputException(String message) {
        super(message);
    }
}