package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * HOTEL-001 旅館圖片資訊
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HOTEL001TranrsHotelImage implements Serializable {

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
     * 檔案名稱
     */
    @JsonProperty("fileName")
    private String fileName;

    /**
     * MIME 類型
     */
    @JsonProperty("mimeType")
    private String mimeType;

    /**
     * 排序順序
     */
    @JsonProperty("sortOrder")
    private Integer sortOrder;
}
