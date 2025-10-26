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
public class MERCH011Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * seller ID
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
     * mediaId
     */
    @JsonProperty("mediaId")
    private String mediaId;
}

