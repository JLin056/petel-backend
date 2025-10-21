package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BOOK005TranrqCardInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * cardNo
     */
    @JsonProperty("card_no")
    @NotNull(message = "信用卡卡號必填")
    private String cardNo;

    /**
     * cardValidMM
     */
    @JsonProperty("card_valid_mm")
    @NotNull(message = "信用卡有效月份必填")
    private String cardValidMM;

    /**
     * cardValidYY
     */
    @JsonProperty("card_valid_yy")
    @NotNull(message = "信用卡有效年份必填")
    private String cardValidYY;

    /**
     * cardCVV2
     */
    @JsonProperty("card_cvv_2")
    @NotNull(message = "信用卡安全碼必填")
    private String cardCVV2;
}