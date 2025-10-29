package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class MERCH013TranrsProperty implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * propertyId
     */
    @JsonProperty("id")
    private String id;

    /**
     * property name
     */
    @JsonProperty("name")
    private String name;

    /**
     * tel
     */
    @JsonProperty("tel")
    private String tel;

    /**
     * address
     */
    @JsonProperty("address")
    private String address;
}