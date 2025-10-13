package com.example.petel.model.book;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
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

    // TODO check this method

    /**
     * AES 128 位元 CBC PKCS7 加密 (Java預設PKCS5Padding與PKCS7Padding行為相同) Ref: <a href="https://developers.ecpay.com.tw/?p=45948"> 方法邏輯參考 </a>
     * @param data      Json 字串 (前端實作：JSON.stringify())
     * @param hashKey   AES密鑰 (16字元)
     * @param hashIV    初始向量 (16字元)
     * @return          Base64編碼的加密字串
     */
    public static String dataEncrypt(String data, String hashKey, String hashIV) throws Exception {

        String urlEncodedData = URLEncoder.encode(data, StandardCharsets.UTF_8);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(hashKey.getBytes(StandardCharsets.UTF_8), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(hashIV.getBytes(StandardCharsets.UTF_8));
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        byte[] encrypted = cipher.doFinal(urlEncodedData.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(encrypted);
    }
}