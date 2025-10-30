package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 修改旅館資訊請求 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // 不送欄位就不覆蓋
public class MERCH007Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * propertyId
     */
    @JsonProperty("id")
    private String id;

    /**
     * name（前端顯示，但不更新）
     */
    @JsonProperty("name")
    private String name;

    /**
     * 營業編號（前端顯示，但不更新）
     */
    @JsonProperty("businessCode")
    private String businessCode;

    /**
     * tel
     */
    @JsonProperty("tel")
    private String tel;

    /**
     * city (縣市)
     */
    @JsonProperty("city")
    private String city;

    /**
     * district (區域)
     */
    @JsonProperty("district")
    private String district;

    /**
     * addressDetail (詳細地址)
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
}
