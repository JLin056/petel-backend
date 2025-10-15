package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AUTH002Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * email
     */
    @JsonProperty("email")
    @NotBlank(message = "Email 不得為空")
    private String email;

    /**
     * password
     */
    @JsonProperty("password")
    @NotBlank(message = "password 不得為空")
    private String password;

    /**
     * role
     */
    @JsonProperty("role")
    @NotBlank(message = "role 不得為空")
    private String role;

}