package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MERCH004Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * property id
     */
    @JsonProperty("propertyId")
    @NotBlank(message = "propertyId不得為空")
    private String propertyId;

    /**
     * name
     */
    @JsonProperty("name")
    @NotBlank(message = "name不得為空")
    private String name;

    /**
     * total units
     */
    @JsonProperty("totalUnits")
    @NotNull(message = "totalUnits不得為空")
    private Integer totalUnits;

    /**
     * base price
     */
    @JsonProperty("basePrice")
    @NotNull(message = "basePrice不得為空")
    private Integer basePrice;

    /**
     * pet type id
     */
    @JsonProperty("petTypeId")
    @NotBlank(message = "petTypeId不得為空")
    private String petTypeId;

    /**
     * info
     */
    @JsonProperty("info")
    @NotBlank(message = "info不得為空")
    private String info;

    /**
     * room size
     */
    @JsonProperty("roomSize")
    @NotBlank(message = "roomSize不得為空")
    private String roomSize;
}
