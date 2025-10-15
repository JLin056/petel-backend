package com.example.petel.exception;

/**
 * 付款異常
 */
public class PaymentFailedException extends Exception {

    public PaymentFailedException() {
    }

    public PaymentFailedException(String message) {
        super(message);
    }
}