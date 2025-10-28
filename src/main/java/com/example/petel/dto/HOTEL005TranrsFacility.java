package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HOTEL005TranrsFacility implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 設施 ID
     */
    @JsonProperty("facilityId")
    private String facilityId;

    /**
     * 設施名稱
     */
    @JsonProperty("facilityName")
    private String facilityName;
}
