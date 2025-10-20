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
public class BOOK005Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * orderId
     */
    @JsonProperty("order_id")
    @NotNull(message = "order_id不得為空")
    @Pattern(regexp = "^O\\d{9}$", message = "請輸入正確格式的order_id")
    private String orderId;

    /**
     * cardInfo
     */
    @JsonProperty("card_info")
    @Valid
    private BOOK005TranrqCardInfo cardInfo;

    /**
     * consumerInfo
     */
    @JsonProperty("consumer_info")
    @Valid
    private BOOK005TranrqConsumerInfo consumerInfo;
}