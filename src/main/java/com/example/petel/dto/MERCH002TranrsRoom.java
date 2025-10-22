package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class MERCH002TranrsRoom implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * room name
     */
    @JsonProperty("name")
    private String name;

    /**
     * total units
     */
    @JsonProperty("totalUnits")
    private Integer totalUnits;

    /**
     * base price
     */
    @JsonProperty("basePrice")
    private Integer basePrice;

    /**
     * info
     */
    @JsonProperty("info")
    private String info;

    /**
     * room size
     */
    @JsonProperty("roomSize")
    private String roomSize;
}