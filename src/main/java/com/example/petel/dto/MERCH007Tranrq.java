package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class MERCH007Tranrq implements Serializable {

    @Serial
    private static final long SerialVersionUID = 1L;

    /**
     * property id
     */
    @JsonProperty("id")
    @NotBlank(message = "id不得為空")
    private String id;

    /**
     * name
     */
    @JsonProperty("name")
    private String name;

    /**
     * tel
     */
    @JsonProperty("tel")
    private String tel;

    /**
     * postal code
     */
    @JsonProperty("postalCode")
    private String postalCode;

    /**
     * address
     */
    @JsonProperty("address")
    private String address;

    /**
     * bank account
     */
    @JsonProperty("bankAccount")
    private String bankAccount;

    /**
     * info
     */
    @JsonProperty("info")
    private String info;

    /**
     * check notice
     */
    @JsonProperty("checkNotice")
    private String checkNotice;

    /**
     * pet notice
     */
    @JsonProperty("petNotice")
    private String petNotice;

    /**
     * property notice
     */
    @JsonProperty("propertyNotice")
    private String propertyNotice;
}
