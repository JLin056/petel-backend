package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * NOTIFY-006 補發錯過的事件 Response
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NOTIFY006Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 補發的事件數量
     */
    @JsonProperty("resent_count")
    private Integer resentCount;
}
