package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * MEDIA-001 單個媒體 Base64 上傳資訊
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MEDIA001TranrqMediaInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Base64 編碼的圖片資料
     */
    @NotBlank(message = "Base64 資料不得為空")
    @JsonProperty("base64Data")
    private String base64Data;

    /**
     * 檔案名稱
     */
    @NotBlank(message = "檔案名稱不得為空")
    @JsonProperty("fileName")
    private String fileName;

    /**
     * 檔案類型 (MIME type)
     */
    @NotBlank(message = "檔案類型不得為空")
    @JsonProperty("mimeType")
    private String mimeType;

    /**
     *  Bucket 名稱
     */
    @NotBlank(message = "Bucket 不得為空")
    @JsonProperty("bucket")
    private String bucket;

    /**
     * 檔案大小 (bytes, 選填，可從 base64 計算)
     */
    @JsonProperty("sizeBytes")
    private Long sizeBytes;

    /**
     * 可見性 (PUBLIC, PRIVATE)
     */
    @JsonProperty("visibility")
    private String visibility;

    /**
     * 排序順序 (選填，若前端有排序功能可指定)
     */
    @JsonProperty("sortOrder")
    private Integer sortOrder;
}

