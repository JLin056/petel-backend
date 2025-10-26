package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * MEDIA-002 單個媒體 Base64 更新資訊
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MEDIA002TranrqMediaInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 媒體ID (必填，用於識別要更新的媒體)
     */
    @NotBlank(message = "媒體ID不得為空")
    @JsonProperty("mediaId")
    private String mediaId;

    /**
     * Base64 編碼的圖片資料 (選填，如果要更新)
     */
    @JsonProperty("base64Data")
    private String base64Data;

    /**
     * 檔案名稱 (選填，如果要更新)
     */
    @JsonProperty("fileName")
    private String fileName;

    /**
     * 檔案類型 (MIME type) (選填，如果要更新)
     */
    @JsonProperty("mimeType")
    private String mimeType;

    /**
     * Bucket 名稱 (選填，如果要更新)
     */
    @JsonProperty("bucket")
    private String bucket;

    /**
     * 檔案大小 (bytes) (選填，如果要更新)
     */
    @JsonProperty("sizeBytes")
    private Long sizeBytes;

    /**
     * 排序順序 (選填，如果要更新關聯表中的順序)
     */
    @JsonProperty("sortOrder")
    private Integer sortOrder;
}
