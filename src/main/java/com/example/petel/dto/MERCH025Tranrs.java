package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MERCH025Tranrs {

    @JsonProperty("facilityId")
    private String id;

    @JsonProperty("facilityName")
    private String name;
}