package com.example.petel.exception;

/**
 * 更新資料異常
 */
public class UpdateFailException extends Exception {

    public UpdateFailException() {
    }

    public UpdateFailException(String message) {
        super(message);
    }
}