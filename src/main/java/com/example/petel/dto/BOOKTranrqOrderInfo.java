package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class BOOKTranrqOrderInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("user_id")
    @NotNull(message = "user_id不得為空")
    private Long userId;

    @JsonProperty("property_id")
    @NotNull(message = "property_id不得為空")
    private Long propertyId;

    @JsonProperty("payment_id")
    @NotNull(message = "payment_id不得為空")
    private Integer paymentId;

    @JsonProperty("hotel_charges")
    @NotNull(message = "hotel_charges不得為空")
    private Double hotelCharges;

    @JsonProperty("check_in")
    @NotNull(message = "check_in不得為空")
    private String checkIn;

    @JsonProperty("check_out")
    @NotNull(message = "check_out不得為空")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "日期格式錯誤，須為 yyyy-MM-dd")
    private String checkOut;

    @JsonProperty("status")
    @NotNull(message = "status不得為空")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "日期格式錯誤，須為 yyyy-MM-dd")
    private String status;

    @JsonProperty("note")
    private String note;
}

