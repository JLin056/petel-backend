package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ADMIN007TranrsTranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 使用者 ID (必填)
     */
    @JsonProperty("USER_ID")
    private String usersId;

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
