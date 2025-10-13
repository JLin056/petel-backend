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

    /**  */
    private String signedUrl;

    /**  */
    private String objectKey;

    /**   */
    private String objectUrl;

}
