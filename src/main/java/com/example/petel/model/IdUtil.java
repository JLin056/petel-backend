package com.example.petel.model;

public class IdUtil {

    public static String generateTableId (String letter, String numberString) {
        return letter + String.format("%09d", Integer.parseInt(numberString) + 1);
    }

    public static String generateFirstTableId (String letter) {
        return letter + "000000001";
    }
}
