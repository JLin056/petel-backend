package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class REVIEW001Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * reviewId
     */
    @JsonProperty("reviewId")
    private String reviewId;

    /**
     * createAt
     */
    @JsonProperty("createAt")
    private LocalDateTime createAt;
}
