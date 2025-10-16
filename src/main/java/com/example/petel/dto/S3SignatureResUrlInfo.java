package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 單個檔案的上傳 URL 資訊 S3SignatureResUrlInfo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class S3SignatureResUrlInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 檔案索引 (對應請求中的檔案順序)
     */
    @JsonProperty("index")
    private Integer index;

    /**
     * 檔案名稱
     */
    @JsonProperty("filename")
    private String filename;

    /**
     * Presigned URL (用於上傳)
     */
    @JsonProperty("presignedUrl")
    private String presignedUrl;

    /**
     * S3 Object Key
     */
    @JsonProperty("objectKey")
    private String objectKey;

    /**
     * 公開存取 URL
     */
    @JsonProperty("publicUrl")
    private String publicUrl;
}
