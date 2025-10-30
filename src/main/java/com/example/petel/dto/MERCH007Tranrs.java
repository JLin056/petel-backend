package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MERCH007Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * propertyId
     */
    @JsonProperty("id")
    private String id;

    /**
     * name
     */
    @JsonProperty("name")
    private String name;

    /**
     * 營業編號
     */
    @JsonProperty("businessCode")
    private String businessCode;

    /**
     * tel
     */
    @JsonProperty("tel")
    private String tel;

    /**
     * city
     */
    @JsonProperty("city")
    private String city;

    /**
     * district
     */
    @JsonProperty("district")
    private String district;

    /**
     * addressDetail
     */
    @JsonProperty("addressDetail")
    private String addressDetail;

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
