package com.example.petel.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 獲取上傳簽名 Resquest
 */
@Data
public class S3SignUploadReq implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 檔案名稱
     */
    private String fileName;

    /**
     * 檔案類型 (MIME type)
     */
    private String fileType;

    /**
     * 住宿ID
     */
    private String lodgingId;
}
