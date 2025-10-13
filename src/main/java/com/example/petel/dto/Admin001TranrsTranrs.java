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
    @JsonProperty("propertyId")
    private Integer propertyId;

    /**
     * 物業名稱
     */
    @JsonProperty("propertyName")
    private String propertyName;

    /**
     * 電話
     */
    @JsonProperty("tel")
    private String tel;

    /**
     * 郵遞區號
     */
    @JsonProperty("postalCode")
    private String postalCode;

    /**
     * 地址
     */
    @JsonProperty("address")
    private String address;

    /**
     * 銀行帳號
     */
    @JsonProperty("bankAccount")
    private String bankAccount;

    /**
     * 賣家名稱
     */
    @JsonProperty("sellerName")
    private String sellerName;

    /**
     * 賣家統編
     */
    @JsonProperty("businessCode")
    private String businessCode;
}
