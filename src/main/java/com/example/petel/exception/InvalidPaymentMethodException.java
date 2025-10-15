package com.example.petel.exception;

/**
 * 付款方式異常
 */
public class InvalidPaymentMethodException extends Exception {

    public InvalidPaymentMethodException() {
    }

    public InvalidPaymentMethodException(String message) {
        super(message);
    }
}