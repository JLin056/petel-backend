package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class BOOKTranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("order_info")
    private BOOKTranrqOrderInfo orderInfo;

    @JsonProperty("order_detail")
    private List<BOOKTranrqOrderDetail> orderDetail;

    public BOOKTranrs() {
    }

    public BOOKTranrs(Long orderId) {
        this.orderId = orderId;
        this.orderInfo = null;
        this.orderDetail = null;
    }

    public BOOKTranrs(Long orderId, BOOKTranrqOrderInfo orderInfo, List<BOOKTranrqOrderDetail> orderDetail) {
        this.orderId = orderId;
        this.orderInfo = orderInfo;
        this.orderDetail = orderDetail;
    }
}