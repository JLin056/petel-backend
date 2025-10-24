package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AUTH004Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * resetUrl
     */
    @JsonProperty("resetUrl")
    private String resetUrl;
}
