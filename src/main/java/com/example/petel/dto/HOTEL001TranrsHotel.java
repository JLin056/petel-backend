package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class HOTEL001TranrsHotel implements Serializable {

    /**
     * 旅館ID
     */
    @JsonProperty("propertyId")
    @JsonAlias({"PROPERTY_ID"})
    private String propertyId;

    /**
     * 旅館名稱
     */
    @JsonProperty("name")
    @JsonAlias({"NAME"})
    private String name;

    /**
     * 地址
     */
    @JsonProperty("address")
    @JsonAlias({"ADDRESS"})
    private String address;

    /**
     * 基本資訊
     */
    @JsonProperty("info")
    @JsonAlias({"INFO"})
    private String info;

    /**
     * 平均評價（0-5分）
     */
    @JsonProperty("avgRating")
    @JsonAlias({"AVG_RATING"})
    private double avgRating;

    /**
     * 最低價格（入住期間）
     */
    @JsonProperty("minPrice")
    @JsonAlias({"MIN_PRICE"})
    private BigDecimal minPrice;

    /**
     * 旅館圖片列表（Media ID）
     */
    @JsonProperty("images")
    @JsonAlias({"IMAGES"})
    private List<String> images;

    /**
     * 城市
     */
    @JsonProperty("city")
    @JsonAlias({"CITY"})
    private String city;

    /**
     * 郵遞區號
     */
    @JsonProperty("postalCode")
    @JsonAlias({"POSTAL_CODE"})
    private String postalCode;

    /**
     * 可用數量
     */
    @JsonProperty("availableQty")
    @JsonAlias({"AVAILABLE_QTY"})
    private Integer availableQty;
}
