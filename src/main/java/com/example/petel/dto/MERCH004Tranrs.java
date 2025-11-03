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
public class MERCH004Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * roomId
     */
    @JsonProperty("id")
    private String id;

    /**
     * propertyId
     */
    @JsonProperty("propertyId")
    private String propertyId;

    /**
     * name
     */
    @JsonProperty("name")
    private String name;

    /**
     * totalUnits
     */
    @JsonProperty("totalUnits")
    private String totalUnits;

    /**
     * basePrice
     */
    @JsonProperty("basePrice")
    private String basePrice;

    /**
     * petTypeId
     */
    @JsonProperty("petTypeId")
    private String petTypeId;

    /**
     * info
     */
    @JsonProperty("info")
    private String info;

    /**
     * roomSize
     */
    @JsonProperty("roomSize")
    private String roomSize;
}
