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
public class HOTEL005TranrsSingleHotelDetail implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 旅館名稱
     */
    @JsonProperty("name")
    private String name;

    /**
     * 地址
     */
    @JsonProperty("address")
    private String address;

    /**
     * 旅館資訊
     */
    @JsonProperty("info")
    private String info;

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
    private List<HOTEL005TranrsImage> propertyImages;

    /**
     * 房型列表
     */
    @JsonProperty("rooms")
    private List<HOTEL005TranrsRoom> rooms;

    /**
     * 評價列表
     */
    @JsonProperty("reviews")
    private List<HOTEL005TranrsReview> reviews;
}
