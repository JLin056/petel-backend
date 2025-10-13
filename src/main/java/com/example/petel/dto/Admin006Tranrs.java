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
public class Admin006Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 刪除的物業 ID
     */
    @JsonProperty("propertyId")
    private Integer propertyId;

    /**
     * 刪除結果訊息
     */
    @JsonProperty("message")
    private String message;
}
