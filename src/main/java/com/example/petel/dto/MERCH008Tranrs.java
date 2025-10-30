package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MERCH008Tranrs implements Serializable {

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
    private String sellerId;

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
     * business code
     */
    @JsonProperty("businessCode")
    private String businessCode;

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

    /**
     * facilities
     */
    @JsonProperty("facilities")
    private List<String> facilities;
}