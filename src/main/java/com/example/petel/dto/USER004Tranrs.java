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
public class USER004Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * user ID
     */
    @JsonProperty("id")
    private String id;

    /**
     * account ID
     */
    @JsonProperty("accountId")
    private String accountId;

    /**
     * name
     */
    @JsonProperty("name")
    private String name;

    /**
     * phone
     */
    @JsonProperty("phone")
    private String phone;

    /**
     * email
     */
    @JsonProperty("email")
    private String email;

    /**
     * mediaBase64
     */
    @JsonProperty("mediaBase64")
    private String mediaBase64;
}

