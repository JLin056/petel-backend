package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class USER007Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * header
     */
    @JsonProperty("header")
    private USER007TranrsHeader header;

    /**
     * items
     */
    @JsonProperty("items")
    private List<USER007TranrsItems> items;

    /**
     * nights
     */
    @JsonProperty("nights")
    private Integer nights;

    /**
     * totalQuantity
     */
    @JsonProperty("totalQuantity")
    private Integer totalQuantity;

    /**
     * totalAmount
     */
    @JsonProperty("totalAmount")
    private Integer totalAmount;
}
