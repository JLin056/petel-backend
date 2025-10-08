package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BOOKTranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("order_info")
    private BOOKTranrqOrderInfo orderInfo;

    @JsonProperty("order_detail")
    private List<BOOKTranrqOrderDetail> orderDetail;

    public BOOKTranrs(Long orderId) {
        this.orderId = orderId;
        this.orderInfo = null;
        this.orderDetail = null;
    }
}