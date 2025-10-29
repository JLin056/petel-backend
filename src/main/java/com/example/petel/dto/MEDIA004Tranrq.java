package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * MEDIA-004 Base64 圖片查詢 Request (支援多種查詢方式)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MEDIA004Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 要查詢的媒體ID列表 (選填，優先使用)
     */
    @JsonProperty("mediaIds")
    private List<String> mediaIds;

    /**
     * 旅館ID (選填)
     */
    @JsonProperty("propertyId")
    private String propertyId;

    /**
     * 房型ID (選填)
     */
    @JsonProperty("roomId")
    private String roomId;

    /**
     * 帳號ID (選填)
     */
    @JsonProperty("accountId")
    private String accountId;

    /**
     * 依 Bucket 查詢 (選填，當其他參數為空時使用)
     */
    @JsonProperty("bucket")
    private String bucket;
}
