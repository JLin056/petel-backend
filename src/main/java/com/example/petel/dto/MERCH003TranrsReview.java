package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class MERCH003TranrsReview implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * order id
     */
    @JsonProperty("orderId")
    private String orderId;

    /**
     * username
     */
    @JsonProperty("userName")
    private String userName;

    /**
     * price score
     */
    @JsonProperty("priceScore")
    private Double priceScore;

    /**
     * environment score
     */
    @JsonProperty("envScore")
    private Double envScore;

    /**
     * service score
     */
    @JsonProperty("serviceScore")
    private Double serviceScore;

    /**
     * content
     */
    @JsonProperty("content")
    private String content;

    /**
     * created at
     */
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
}