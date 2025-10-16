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