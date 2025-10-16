package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * IMG-001 單個媒體上傳結果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IMG001TranrsMediaResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 媒體ID (varchar2(10))
     */
    @JsonProperty("mediaId")
    private String mediaId;

    /**
     * S3 Bucket
     */
    @JsonProperty("bucket")
    private String bucket;

    /**
     * S3 Object Key
     */
    @JsonProperty("objectKey")
    private String objectKey;

    /**
     * 公開存取 URL
     */
    @JsonProperty("objectUrl")
    private String objectUrl;

    /**
     * 檔案大小
     */
    @JsonProperty("sizeBytes")
    private Long sizeBytes;

    /**
     * 檔案類型
     */
    @JsonProperty("mimeType")
    private String mimeType;

    /**
     * 可見性
     */
    @JsonProperty("visibility")
    private String visibility;

    /**
     * 上傳狀態 (SUCCESS, FAILED)
     */
    @JsonProperty("status")
    private String status;

    /**
     * 錯誤訊息 (如果失敗)
     */
    @JsonProperty("errorMessage")
    private String errorMessage;
}
