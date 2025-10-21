package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BOOK002Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("property_id")
    private String propertyId;

    @JsonProperty("payment_id")
    private String paymentId;

    @JsonProperty("hotel_charges")
    private Integer hotelCharges;

    @JsonProperty("check_in")
    private String checkIn;

    @JsonProperty("check_out")
    private String checkOut;

    @JsonProperty("status")
    private String status;

    @JsonProperty("guest_name")
    private String guestName;

    @JsonProperty("guest_phone")
    private String guestPhone;

    @JsonProperty("note")
    private String note;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("order_detail")
    private List<BOOKTranrqOrderDetail> orderDetail;
}