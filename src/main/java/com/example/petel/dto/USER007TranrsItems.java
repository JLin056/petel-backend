package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class USER007TranrsItems implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * itemId
     */
    @JsonProperty("itemId")
    private String itemId;

    /**
     * roomId
     */
    @JsonProperty("roomId")
    private String roomId;

    /**
     * roomName
     */
    @JsonProperty("roomName")
    private String roomName;

    /**
     * arrivalDate
     */
    @JsonProperty("arrivalDate")
    private LocalDate arrivalDate;

    /**
     * quantity
     */
    @JsonProperty("quantity")
    private Integer quantity;

    /**
     * price
     */
    @JsonProperty("price")
    private Integer price;
}
