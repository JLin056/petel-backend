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
 * IMG-002 單個媒體更新資訊
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IMG002TranrqMediaInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 媒體ID (要更新的圖片ID, varchar2(10))
     */
    @NotBlank(message = "媒體ID不得為空")
    @JsonProperty("mediaId")
    private String mediaId;

    /**
     * 新的 S3 Object Key
     */
    @NotBlank(message = "新的 Object Key 不得為空")
    @JsonProperty("newObjectKey")
    private String newObjectKey;

    /**
     * 新的 S3 Bucket
     */
    @NotBlank(message = "新的 Bucket 不得為空")
    @JsonProperty("newBucket")
    private String newBucket;

    /**
     * 新的檔案類型 (MIME type)
     */
    @NotBlank(message = "檔案類型不得為空")
    @JsonProperty("mimeType")
    private String mimeType;

    /**
     * 新的檔案大小 (bytes)
     */
    @NotNull(message = "檔案大小不得為空")
    @JsonProperty("sizeBytes")
    private Long sizeBytes;

    /**
     * 新的可見性 (PUBLIC, PRIVATE)
     */
    @JsonProperty("visibility")
    private String visibility;
}
