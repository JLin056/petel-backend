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
public class ADMIN004Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 訂單 ID
     */
    @JsonProperty("orderId")
    private String orderId;

    /**
     * 更新後的備註
     */
    @JsonProperty("note")
    private String note;

    /**
     * 更新時間
     */
    @JsonProperty("updatedAt")
    private String updatedAt;
}
