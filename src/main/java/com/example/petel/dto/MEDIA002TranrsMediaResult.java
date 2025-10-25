package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * MEDIA-002 單個媒體 Base64 更新結果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MEDIA002TranrsMediaResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 媒體ID
     */
    @JsonProperty("mediaId")
    private String mediaId;

    /**
     * Bucket
     */
    @JsonProperty("bucket")
    private String bucket;

    /**
     * 檔案名稱
     */
    @JsonProperty("fileName")
    private String fileName;

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
     * 更新狀態 (SUCCESS, FAILED)
     */
    @JsonProperty("status")
    private String status;

    /**
     * 錯誤訊息 (如果失敗)
     */
    @JsonProperty("errorMessage")
    private String errorMessage;
}
