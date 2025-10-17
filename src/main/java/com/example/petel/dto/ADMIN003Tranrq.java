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
public class ADMIN003Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 訂單日期 (ARRIVAL_DATE)
     */
    @JsonProperty("date")
    private String date;

    /**
     * 會員姓名
     */
    @JsonProperty("userName")
    private String userName;

    /**
     * 會員電話
     */
    @JsonProperty("userPhone")
    private String userPhone;

    /**
     * 旅館名稱
     */
    @JsonProperty("propertyName")
    private String propertyName;

    /**
     * 旅館電話
     */
    @JsonProperty("propertyPhone")
    private String propertyPhone;

    /**
     * 分頁資訊
     */
    @JsonProperty("page")
    private ADMIN003TranrqPage page;
}
