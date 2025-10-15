package com.example.petel.exception;

/**
 * 退款異常
 */
public class RefundFailException extends Exception {

    public RefundFailException() {
    }

    public RefundFailException(String message) {
        super(message);
    }
}