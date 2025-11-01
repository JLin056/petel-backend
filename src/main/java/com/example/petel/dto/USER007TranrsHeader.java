package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class USER007TranrsHeader implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * orderId
     */
    @JsonProperty("orderId")
    private String orderId;

    /**
     * status
     */
    @JsonProperty("status")
    private String status;

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
     * hotelCharges
     */
    @JsonProperty("hotelCharges")
    private Integer hotelCharges;

    /**
     * paymentId
     */
    @JsonProperty("paymentId")
    private String paymentId;

    /**
     * paymentName
     */
    @JsonProperty("paymentName")
    private String paymentName;

    /**
     * propertyId
     */
    @JsonProperty("propertyId")
    private String propertyId;

    /**
     * propertyName
     */
    @JsonProperty("propertyName")
    private String propertyName;

    /**
     * propertyTel
     */
    @JsonProperty("propertyTel")
    private String propertyTel;

    /**
     * propertyAddress
     */
    @JsonProperty("propertyAddress")
    private String propertyAddress;

    /**
     * propertyImageUrl
     */
    @JsonProperty("propertyImageUrl")
    private String propertyImageUrl;

    /**
     * 是否是 guest（y/n）
     */
    @JsonProperty("guest")
    private String guest;

    /**
     * guestName
     */
    @JsonProperty("guestName")
    private String guestName;

    /**
     * guestPhone
     */
    @JsonProperty("guestPhone")
    private String guestPhone;

    /**
     * note
     */
    @JsonProperty("note")
    private String note;
}
