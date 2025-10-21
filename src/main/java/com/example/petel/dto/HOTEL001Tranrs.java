package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class HOTEL001Tranrs<T> implements Serializable {

    /**
     * 每頁筆數
     */
    @JsonProperty("pageSize")
    private Integer pageSize;

    /**
     * 當前頁碼
     */
    @JsonProperty("pageNumber")
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

    /**
     * 旅館列表
     */
    @JsonProperty("hotels")
    private List<T> hotels;
}
