package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HOTEL006TranrsSingleHotelDetail implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 旅館名稱
     */
    @JsonProperty("name")
    private String name;

    /**
     * sellerId
     */
    @JsonProperty("sellerId")
    private String sellerId;

    /**
     * 電話
     */
    @JsonProperty("tel")
    private String tel;

    /**
     * 地址
     */
    @JsonProperty("address")
    private String address;

    /**
     * postalCode
     */
    @JsonProperty("postalCode")
    private String postalCode;

    /**
     * 銀行帳號
     */
    @JsonProperty("bankAccount")
    private String bankAccount;

    /**
     * 營業登記號碼
     */
    @JsonProperty("businessCode")
    private String businessCode;


    /**
     * 旅館資訊
     */
    @JsonProperty("info")
    private String info;

    /**
     * 入住須知
     */
    @JsonProperty("checkNotice")
    private String checkNotice;

    /**
     * 寵物入住注意事項
     */
    @JsonProperty("petNotice")
    private String petNotice;

    /**
     * 旅館注意事項
     */
    @JsonProperty("propertyNotice")
    private String propertyNotice;

    /**
     * 平均評分
     */
    @JsonProperty("avgRating")
    private Double avgRating;

    /**
     * 評價總數
     */
    @JsonProperty("reviewCount")
    private Integer reviewCount;

    /**
     * 房型總數
     */
    @JsonProperty("roomCount")
    private Integer roomCount;

    /**
     * 城市
     */
    @JsonProperty("city")
    private String city;

    /**
     * 行政區
     */
    @JsonProperty("district")
    private String district;

    /**
     * 旅館圖片列表
     */
    @JsonProperty("propertyImages")
    private List<HOTEL006TranrsImage> propertyImages;

    /**
     * 房型列表
     */
    @JsonProperty("rooms")
    private List<HOTEL006TranrsRoom> rooms;

    /**
     * 評價列表
     */
    @JsonProperty("reviews")
    private List<HOTEL006TranrsReview> reviews;

    /**
     * 設施列表
     */
    @JsonProperty("facilities")
    private List<HOTEL006TranrsFacility> facilities;
}
