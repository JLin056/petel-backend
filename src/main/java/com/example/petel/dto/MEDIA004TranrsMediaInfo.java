package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * MEDIA-004 單個媒體 Base64 查詢結果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MEDIA004TranrsMediaInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 媒體ID
     */
    @JsonProperty("mediaId")
    private String mediaId;

    /**
     * Base64 編碼的圖片資料
     */
    @JsonProperty("base64Data")
    private String base64Data;

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
     * 創建時間
     */
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    /**
     * 更新時間
     */
    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;

    /**
     * 排序順序 (僅在透過 propertyId 或 roomId 查詢時有值)
     */
    @JsonProperty("sortOrder")
    private Integer sortOrder;
}
