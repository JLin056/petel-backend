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
public class Admin007TranrsTranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 會員 ID
     */
    @JsonProperty("ACCOUNT_ID")
    private String accountID;

    /**
     * Email
     */
    @JsonProperty("EMAIL")
    private String email;

    /**
     * 姓名
     */
    @JsonProperty("NAME")
    private String name;

    /**
     * 電話
     */
    @JsonProperty("PHONE")
    private String phone;

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
