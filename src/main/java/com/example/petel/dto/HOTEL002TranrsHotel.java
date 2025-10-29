package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class HOTEL002TranrsHotel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * property name
     */
    @JsonProperty("name")
    private String name;

    /**
     * tel
     */
    @JsonProperty("tel")
    private String tel;

    /**
     * business code
     */
    @JsonProperty("businessCode")
    private String businessCode;

    /**
     * bank account
     */
    @JsonProperty("bankAccount")
    private String bankAccount;

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