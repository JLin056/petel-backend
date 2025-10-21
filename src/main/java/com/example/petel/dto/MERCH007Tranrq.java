package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class MERCH007Tranrq implements Serializable {

    @Serial
    private static final long SerialVersionUID = 1L;

    /**
     * name
     */
    @JsonProperty("name")
    private String name;

    /**
     * tel
     */
    @JsonProperty("tel")
    @Pattern(regexp = "^[0-9+\\-()\\s]{6,20}$", message = "phone格式不正確")
    private String tel;

    /**
     * postal code
     */
    @JsonProperty("postalCode")
    @Pattern(regexp = "^[0-9]{3}$", message = "postalCode為3碼")
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
    @Pattern(regexp = "^[0-9]{17}$", message = "bankAccount含銀行編號共17碼")
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
