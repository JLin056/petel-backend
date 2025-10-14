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
public class Admin002TranrsTranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 賣家 ID
     */
    @JsonProperty("SELLER_ID")
    private String sellerId;

    /**
     * 帳號 ID
     */
    @JsonProperty("ACCOUNT_ID")
    private String accountId;

    /**
     * Email
     */
    @JsonProperty("EMAIL")
    private String email;

    /**
     * 賣家名稱
     */
    @JsonProperty("NAME")
    private String name;

    /**
     * 統編
     */
    @JsonProperty("BUSINESS_CODE")
    private String businessCode;

    /**
     * 角色
     */
    @JsonProperty("ROLE")
    private String role;

    /**
     * 帳號狀態
     */
    @JsonProperty("STATUS")
    private String status;
}
