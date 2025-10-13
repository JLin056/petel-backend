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
     * 帳號 ID
     */
    @JsonProperty("accountId")
    private Integer accountId;

    /**
     * Email
     */
    @JsonProperty("email")
    private String email;

    /**
     * 姓名
     */
    @JsonProperty("name")
    private String name;

    /**
     * 電話
     */
    @JsonProperty("phone")
    private String phone;

    /**
     * 角色
     */
    @JsonProperty("role")
    private String role;

    /**
     * 帳號狀態
     */
    @JsonProperty("status")
    private String status;
}
