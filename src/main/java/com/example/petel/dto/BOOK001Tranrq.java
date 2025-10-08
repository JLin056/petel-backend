package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class BOOK001Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Valid
    @JsonProperty("order_info")
    private BOOKTranrqOrderInfo orderInfo;

    @Valid
    @JsonProperty("order_detail")
    private List<BOOKTranrqOrderDetail> orderDetail;
}

