package com.example.petel.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * 獲取上傳簽名 Response
 */
@Data
public class S3SignatureRes implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /** 預簽名上傳 URL (預先授權網址) */
    private String signedUrl;

    /** S3 物件鍵 */
    private String objectKey;

    /** S3 公開存取 URL (公開資源網址) */
    private String objectUrl;

}
