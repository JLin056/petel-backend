package com.example.petel.exception;

/**
 * 未授權異常
 * 用於處理未登入或無權限訪問的情況
 */
public class UnauthorizedException extends Exception {

    public UnauthorizedException() {
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
