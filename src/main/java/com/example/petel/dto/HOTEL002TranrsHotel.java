package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class HOTEL002TranrsHotel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("name")
    private String name;

    @JsonProperty("tel")
    private String tel;

    @JsonProperty("postalCode")
    private String postalCode;

    @JsonProperty("address")
    private String address;
}