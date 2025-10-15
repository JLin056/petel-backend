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
 * IMG-001 單個媒體上傳確認資訊
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IMG001TranrqMediaInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * S3 Object Key (從 presigned URL 回應中取得)
     */
    @NotBlank(message = "Object Key 不得為空")
    @JsonProperty("objectKey")
    private String objectKey;

    /**
     * S3 Bucket 名稱
     */
    @NotBlank(message = "Bucket 不得為空")
    @JsonProperty("bucket")
    private String bucket;

    /**
     * 檔案類型 (MIME type)
     */
    @NotBlank(message = "檔案類型不得為空")
    @JsonProperty("mimeType")
    private String mimeType;

    /**
     * 檔案大小 (bytes)
     */
    @NotNull(message = "檔案大小不得為空")
    @JsonProperty("sizeBytes")
    private Long sizeBytes;

    /**
     * 可見性 (PUBLIC, PRIVATE)
     */
    @JsonProperty("visibility")
    private String visibility;
}
