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
public class AUTH002Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * account ID
     */
    @JsonProperty("AccountId")
    private String accountId;

    /**
     * Email
     */
    @JsonProperty("Email")
    private String email;

    /**
     * Role
     */
    @JsonProperty("Role")
    private String role;
}
