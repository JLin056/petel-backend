package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ADMIN001Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 物業名稱 (模糊搜尋)
     */
    @JsonProperty("propertyName")
    private String propertyName;

    /**
     * 電話 (模糊搜尋)
     */
    @JsonProperty("tel")
    private String tel;

    /**
     * 郵遞區號 (模糊搜尋)
     */
    @JsonProperty("postalCode")
    private String postalCode;

    /**
     * 地址 (模糊搜尋)
     */
    @JsonProperty("address")
    private String address;

    /**
     * 店家名稱 (模糊搜尋)
     */
    @JsonProperty("sellerName")
    private String sellerName;

    /**
     * 分頁資訊
     */
    @JsonProperty("page")
    private ADMIN001TranrqPage page;
}
