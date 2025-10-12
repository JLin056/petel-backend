package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class MERCH003TranrsReview implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("orderId")
    private Long orderId;

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("priceScore")
    private Long priceScore;

    @JsonProperty("envScore")
    private Long envScore;

    @JsonProperty("serviceScore")
    private Long serviceScore;

    @JsonProperty("content")
    private String content;

    @JsonProperty("createdAt")
    private Timestamp createdAt;
}