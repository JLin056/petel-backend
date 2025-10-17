package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 內部使用的查詢結果 DTO（從資料庫取出的原始資料）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ADMIN003QueryResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("ORDER_ID")
    private String orderId;

    @JsonProperty("ORDER_DATE")
    private String date;

    @JsonProperty("GUEST")
    private String guest;

    @JsonProperty("GUEST_NAME")
    private String guestName;

    @JsonProperty("GUEST_PHONE")
    private String guestPhone;

    @JsonProperty("USER_NAME")
    private String userName;

    @JsonProperty("USER_PHONE")
    private String userPhone;

    @JsonProperty("PROPERTY_NAME")
    private String propertyName;

    @JsonProperty("PROPERTY_PHONE")
    private String propertyPhone;

    @JsonProperty("ROOM")
    private String room;

    @JsonProperty("QUANTITY")
    private Integer quantity;

    @JsonProperty("HOTEL_CHARGES")
    private Integer hotelCharges;

    @JsonProperty("STATUS")
    private String status;

    @JsonProperty("NOTE")
    private String note;

    @JsonProperty("CREATED_AT")
    private String createdAt;

    @JsonProperty("UPDATED_AT")
    private String updatedAt;
}
