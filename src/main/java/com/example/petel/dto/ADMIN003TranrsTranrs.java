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
public class ADMIN003TranrsTranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 訂單 ID
     */
    @JsonProperty("ORDER_ID")
    private String orderId;

    /**
     * 訂單日期 (入住日期)
     */
    @JsonProperty("ORDER_DATE")
    private String date;

    /**
     * 會員姓名
     */
    @JsonProperty("USER_NAME")
    private String userName;

    /**
     * 會員電話
     */
    @JsonProperty("USER_PHONE")
    private String userPhone;

    /**
     * 旅館名稱
     */
    @JsonProperty("PROPERTY_NAME")
    private String propertyName;

    /**
     * 旅館電話
     */
    @JsonProperty("PROPERTY_PHONE")
    private String propertyPhone;

    /**
     * 房型名稱
     */
    @JsonProperty("ROOM")
    private String room;

    /**
     * 數量
     */
    @JsonProperty("QUANTITY")
    private Integer quantity;

    /**
     * 訂單總價
     */
    @JsonProperty("HOTEL_CHARGES")
    private Integer hotelCharges;

    /**
     * 付款狀態
     */
    @JsonProperty("STATUS")
    private String status;

    /**
     * 備註
     */
    @JsonProperty("NOTE")
    private String note;

    /**
     * 建立時間
     */
    @JsonProperty("CREATED_AT")
    private String createdAt;

    /**
     * 更新時間
     */
    @JsonProperty("UPDATED_AT")
    private String updatedAt;
}
