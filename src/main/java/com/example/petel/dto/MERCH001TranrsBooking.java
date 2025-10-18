package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class MERCH001TranrsBooking implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * order id
     */
    @JsonProperty("id")
    private String id;

    /**
     * created at
     */
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    /**
     * username
     */
    @JsonProperty("userName")
    private String userName;

    /**
     * check in
     */
    @JsonProperty("checkIn")
    private String checkIn;

    /**
     * check out
     */
    @JsonProperty("checkOut")
    private String checkOut;

    /**
     * payment name
     */
    @JsonProperty("paymentName")
    private String paymentName;

    /**
     * status
     */
    @JsonProperty("status")
    private String status;

    /**
     * hotel charges
     */
    @JsonProperty("hotelCharges")
    private Integer hotelCharges;
}