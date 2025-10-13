package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BOOK001Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * orderInfo
     */
    @Valid
    @JsonProperty("order_info")
    private BOOKTranrqOrderInfo orderInfo;

    /**
     * orderDetail
     */
    @Valid
    @JsonProperty("order_detail")
    private List<BOOKTranrqOrderDetail> orderDetail;
}