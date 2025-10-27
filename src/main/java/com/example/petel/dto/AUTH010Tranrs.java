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
public class AUTH010Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * accessToken
     */
    @JsonProperty("accessToken")
    private String accessToken;
}
