package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * IMG-002 圖片更新 Response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IMG002Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 媒體ID
     */
    @JsonProperty("mediaId")
    private Long mediaId;

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
     * 更新後的可見性
     */
    @JsonProperty("visibility")
    private String visibility;

    /**
     * 儲存類型
     */
    @JsonProperty("storageType")
    private String storageType;
}
