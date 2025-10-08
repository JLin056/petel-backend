package com.example.petel.exception;

/**
 * 資料查詢異常
 */
public class DataNotFoundException extends Exception {

    public DataNotFoundException() {
    }

    public DataNotFoundException(String message) {
        super(message);
    }
}


