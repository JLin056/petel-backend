package com.example.petel.model.book;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;
import java.util.TreeMap;

public class CodeUtil {

    /**
     * Generate CheckMacValue. Ref: <a href="https://developers.ecpay.com.tw/?p=2902"> 方法邏輯參考 </a>
     *
     * @param params  Map<String, Object>
     * @param hashKey String
     * @param hashIV  String
     * @return CheckMacValue for ECPay
     */
    public static String generateCheckMacValue(Map<String, Object> params, String hashKey, String hashIV) throws Exception {

        Map<String, Object> sortedParams = new TreeMap<>(params);

        StringBuilder sb = new StringBuilder();
        sb.append("HashKey=").append(hashKey);
        for (Map.Entry<String, Object> entry : sortedParams.entrySet()) {
            sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
        }
        sb.append("&HashIV=").append(hashIV);

        String encodedString = URLEncoder.encode(sb.toString(), StandardCharsets.UTF_8);

        encodedString = encodedString.replace("%2d", "-")
                .replace("%5f", "_")
                .replace("%2e", ".")
                .replace("%21", "!")
                .replace("%2a", "*")
                .replace("%28", "(")
                .replace("%29", ")")
                .replace("%20", "+");

        MessageDigest md = MessageDigest.getInstance("SHA-256"); // TODO check this part
        md.update(encodedString.toLowerCase().getBytes());
        byte[] digest = md.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : digest) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString().toUpperCase();
    }
}