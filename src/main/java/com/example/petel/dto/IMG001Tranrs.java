package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * IMG-001 圖片上傳 Response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IMG001Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 媒體ID
     */
    @JsonProperty("mediaId")
    private Long mediaId;

    /**
     * S3 Bucket 名稱
     */
    @JsonProperty("bucket")
    private String bucket;

    /**
     * S3 Object Key
     */
    @JsonProperty("objectKey")
    private String objectKey;

    /**
     * 公開存取的完整 URL
     */
    @JsonProperty("objectUrl")
    private String objectUrl;

    /**
     * 檔案大小 (bytes)
     */
    @JsonProperty("sizeBytes")
    private Long sizeBytes;

    /**
     * MIME 類型
     */
    @JsonProperty("mimeType")
    private String mimeType;

    /**
     * 可見性
     */
    @JsonProperty("visibility")
    private String visibility;
}
