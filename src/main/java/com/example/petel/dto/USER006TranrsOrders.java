package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class USER006TranrsOrders implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * orderId
     */
    @JsonProperty("orderId")
    private String orderId;

    /**
     * propertyName
     */
    @JsonProperty("propertyName")
    private String propertyName;

    /**
     * checkIn
     */
    @JsonProperty("checkIn")
    private String checkIn;

    /**
     * checkOut
     */
    @JsonProperty("checkOut")
    private String checkOut;

    /**
     * status
     */
    @JsonProperty("status")
    private String status;

    /**
     * totalPrice
     */
    @JsonProperty("totalPrice")
    private Integer totalPrice;

    /**
     * propertyAvg
     */
    @JsonProperty("propertyAvg")
    private Double propertyAvg;

    /**
     * imageUrl
     */
    @JsonProperty("imageUrl")
    private String imageUrl;

    /**
     * hasReview
     */
    @JsonProperty("hasReview")
    private boolean hasReview;
}
