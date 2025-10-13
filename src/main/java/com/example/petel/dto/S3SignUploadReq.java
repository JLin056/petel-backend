package com.example.petel.dto;

import jakarta.validation.Valid;
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

    /**  */
    private String filename;

    /**  */
    private String fileType;

    /**   */
    private String lodgingId;
}
