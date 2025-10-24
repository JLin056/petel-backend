package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AUTH004Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * email
     */
    @JsonProperty("email")
    @NotBlank(message = "Email 不得為空")
    private String email;
}
