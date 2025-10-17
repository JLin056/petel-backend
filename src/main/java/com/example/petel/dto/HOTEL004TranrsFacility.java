package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class HOTEL004TranrsFacility implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * facility name
     */
    @JsonProperty("name")
    private String name;
}