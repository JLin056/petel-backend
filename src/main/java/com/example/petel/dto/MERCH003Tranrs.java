package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class MERCH003Tranrs<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * avgPriceScore
     */
    @JsonProperty("avgPriceScore")
    private Double avgPriceScore;

    /**
     * avgEnvScore
     */
    @JsonProperty("avgEnvScore")
    private Double avgEnvScore;

    /**
     * avgServiceScore
     */
    @JsonProperty("avgServiceScore")
    private Double avgServiceScore;

    /**
     * reviews
     */
    @JsonProperty("reviews")
    private List<T> reviews;

    /**
     * 當前頁數
     */
    @JsonProperty("currentPage")
    private Integer pageNumber;

    /**
     * 總頁數
     */
    @JsonProperty("totalPage")
    private Integer totalPage;

    /**
     * 總筆數
     */
    @JsonProperty("totalCount")
    private Integer totalCount;
}