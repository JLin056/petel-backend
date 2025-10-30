package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class MERCH008Tranrq implements Serializable {

    @Serial
    private static final long SerialVersionUID = 1L;

    /**
     * property id
     */
    @JsonProperty("id")
    private String id;

    /**
     * seller id
     */
    @JsonProperty("sellerId")
    @NotBlank(message = "sellerId不得為空")
    private String sellerId;

    /**
     * name
     */
    @JsonProperty("name")
    @NotBlank(message = "name不得為空")
    private String name;

    /**
     * tel
     */
    @JsonProperty("tel")
    @NotBlank(message = "tel不得為空")
    private String tel;

    /**
     * business code
     */
    @JsonProperty("businessCode")
    @NotBlank(message = "businessCode不得為空")
    private String businessCode;

    /**
     * postal code
     */
    @JsonProperty("postalCode")
    @NotBlank(message = "postalCode不得為空")
    private String postalCode;

    /**
     * address
     */
    @JsonProperty("address")
    @NotBlank(message = "address不得為空")
    private String address;

    /**
     * bank account
     */
    @JsonProperty("bankAccount")
    @NotBlank(message = "bankAccount不得為空")
    private String bankAccount;

    /**
     * info
     */
    @JsonProperty("info")
    @NotBlank(message = "info不得為空")
    private String info;

    /**
     * check notice
     */
    @JsonProperty("checkNotice")
    @NotBlank(message = "checkNotice不得為空")
    private String checkNotice;

    /**
     * pet notice
     */
    @JsonProperty("petNotice")
    @NotBlank(message = "petNotice不得為空")
    private String petNotice;

    /**
     * property notice
     */
    @JsonProperty("propertyNotice")
    private String propertyNotice;
}
