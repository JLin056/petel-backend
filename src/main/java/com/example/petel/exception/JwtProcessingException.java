package com.example.petel.exception;

/**
 * Jwt 異常
 */
public class JwtProcessingException  extends Exception {

    public JwtProcessingException() {
    }

    public JwtProcessingException(String message) {
        super(message);
    }
}