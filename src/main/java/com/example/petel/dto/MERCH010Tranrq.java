package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class MERCH010Tranrq implements Serializable {

    @Serial
    private static final long SerialVersionUID = 1L;

    /**
     * seller id
     */
    @JsonProperty("id")
    @NotBlank(message = "id不得為空")
    private String id;

    /**
     * account id
     */
    @JsonProperty("accountId")
    @NotBlank(message = "accountId不得為空")
    private String accountId;

    /**
     * name
     */
    @JsonProperty("name")
    @NotBlank(message = "name不得為空")
    private String name;

    /**
     * business code
     */
    @JsonProperty("businessCode")
    @NotBlank(message = "businessCode不得為空")
    private String businessCode;

    /**
     * media id
     */
    @JsonProperty("mediaId")
    private String mediaId;
}
