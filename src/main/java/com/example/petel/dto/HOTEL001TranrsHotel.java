package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class HOTEL001TranrsHotel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Property ID
     */
    @JsonProperty("propertyId")
    private String propertyId;

    /**
     * Store name
     */
    @JsonProperty("storeName")
    private String storeName;

    /**
     * Seller ID
     */
    @JsonProperty("sellerId")
    private String sellerId;

    /**
     * City
     */
    @JsonProperty("city")
    private String city;

    /**
     * Postal code
     */
    @JsonProperty("postalCode")
    private String postalCode;

    /**
     * Telephone
     */
    @JsonProperty("tel")
    private String tel;

    /**
     * Hotel information
     */
    @JsonProperty("Info")
    private String Info;

    /**
     * Address
     */
    @JsonProperty("address")
    private String address;

    /**
     * Rating
     */
    @JsonProperty("rating")
    private BigDecimal rating;

    /**
     * Base price
     */
    @JsonProperty("basePrice")
    private BigDecimal basePrice;

    /**
     * Media ID
     */
    @JsonProperty("mediaId")
    private String mediaId;

    /**
     * Room ID
     */
    @JsonProperty("roomId")
    private String roomId;

    /**
     * Available rooms count
     */
    @JsonProperty("availableRooms")
    private Integer availableRooms;
}
