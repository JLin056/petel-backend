package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class HOTEL001Tranrs<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Page size
     */
    @JsonProperty("pageSize")
    private Integer pageSize;

    /**
     * Page number
     */
    @JsonProperty("pageNumber")
    private Integer pageNumber;

    /**
     * Total pages
     */
    @JsonProperty("totalPage")
    private Integer totalPage;

    /**
     * Total count
     */
    @JsonProperty("totalCount")
    private Integer totalCount;

    /**
     * Hotels list
     */
    @JsonProperty("hotels")
    private List<T> hotels;
}
