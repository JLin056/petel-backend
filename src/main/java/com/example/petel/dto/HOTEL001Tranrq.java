package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HOTEL001Tranrq implements Serializable {

    /**
     * 城市（選填）
     */
    @JsonProperty("city")
    private String city;

    /**
     * 入住日期（選填，格式：YYYY-MM-DD）
     */
    @JsonProperty("checkIn")
    private String checkIn;

    /**
     * 退房日期（選填，格式：YYYY-MM-DD）
     */
    @JsonProperty("checkOut")
    private String checkOut;

    /**
     * 寵物種類ID（必填，例如：W001-貓, W002-迷你犬）
     */
    @JsonProperty("petType")
    @NotBlank(message = "petType is required")
    private String petType;

    /**
     * 寵物數量（選填，預設為1）
     */
    @JsonProperty("petCount")
    private Integer petCount;

    /**
     * 最低價格（選填）
     */
    @JsonProperty("priceMin")
    private BigDecimal priceMin;

    /**
     * 最高價格（選填）
     */
    @JsonProperty("priceMax")
    private BigDecimal priceMax;

    /**
     * 最低評價（選填，0-5分）
     */
    @JsonProperty("minRating")
    private double minRating;

    /**
     * 設施ID列表（選填）
     */
    @JsonProperty("facilities")
    private List<String> facilities;

    /**
     * 分頁資訊（必填）
     */
    @JsonProperty("page")
    @Valid
    private HOTEL001TranrqPage page;
}
