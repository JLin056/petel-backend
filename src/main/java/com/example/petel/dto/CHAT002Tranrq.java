package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CHAT002Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 每頁筆數
     */
    @JsonProperty("pageSize")
    private Integer pageSize = 10;

    /**
     * 當前頁碼
     */
    @JsonProperty("pageNumber")
    private Integer pageNumber = 1;
}
