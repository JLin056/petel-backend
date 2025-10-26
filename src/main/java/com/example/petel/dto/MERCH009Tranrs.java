package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MERCH009Tranrs implements Serializable {

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
     * mediaId
     */
    @JsonProperty("mediaId")
    private String mediaId;
}
