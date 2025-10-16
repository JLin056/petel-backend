package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin001TranrsTranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 物業 ID
     */
    @JsonProperty("PROPERTY_ID")
    private String propertyId;

    /**
     * 物業名稱
     */
    @JsonProperty("PROPERTY_NAME")
    private String propertyName;

    /**
     * 電話
     */
    @JsonProperty("TEL")
    private String tel;

    /**
     * 郵遞區號
     */
    @JsonProperty("POSTAL_CODE")
    private String postalCode;

    /**
     * 地址
     */
    @JsonProperty("ADDRESS")
    private String address;

    /**
     * 銀行帳號
     */
    @JsonProperty("BANK_ACCOUNT")
    private String bankAccount;

    /**
     * 賣家名稱
     */
    @JsonProperty("SELLER_NAME")
    private String sellerName;

    /**
     * 賣家統編
     */
    @JsonProperty("BUSINESS_CODE")
    private String businessCode;
}
