package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HOTEL001Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * City name
     */
    @JsonProperty("city")
    private String city;

    /**
     * Check-in date (format: yyyy-MM-dd)
     */
    @JsonProperty("checkIn")
    private String checkIn;

    /**
     * Check-out date (format: yyyy-MM-dd)
     */
    @JsonProperty("checkOut")
    private String checkOut;

    /**
     * Pet type (e.g., DOG, CAT)
     */
    @JsonProperty("petType")
    private String petType;

    /**
     * Number of pets
     */
    @JsonProperty("petCount")
    @Min(value = 1, message = "petCount must be >= 1")
    private Integer petCount;

    /**
     * Minimum price
     */
    @JsonProperty("priceMin")
    private BigDecimal priceMin;

    /**
     * Maximum price
     */
    @JsonProperty("priceMax")
    private BigDecimal priceMax;

    /**
     * Minimum rating
     */
    @JsonProperty("minRating")
    private Integer minRating;

    /**
     * Pagination info
     */
    @JsonProperty("page")
    @Valid
    private HOTEL001TranrqPage page;
}
