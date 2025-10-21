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
public class ADMIN009Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 刪除的賣家 ID
     */
    @JsonProperty("sellersId")
    private String sellersId;

    /**
     * 一併刪除的物業數量
     */
    @JsonProperty("deletedPropertiesCount")
    private Integer deletedPropertiesCount;

    /**
     * 刪除結果訊息
     */
    @JsonProperty("message")
    private String message;
}
