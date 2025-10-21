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
public class
ADMIN007Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 會員 ID
     */
    @JsonProperty("Account_Id")
    private String accountId;

    /**
     * Email
     */
    @JsonProperty("Email")
    private String email;

    /**
     * 姓名 (支援模糊查詢)
     */
    @JsonProperty("Name")
    private String name;

    /**
     * 電話
     */
    @JsonProperty("Phone")
    private String phone;

    /**
     * 分頁資訊
     */
    @JsonProperty("page")
    private PageRequest page;
}
