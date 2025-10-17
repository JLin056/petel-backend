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
public class ADMIN002TranrqPage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 頁碼
     */
    @JsonProperty("pageNumber")
    private Integer pageNumber;

    /**
     * 每頁筆數
     */
    @JsonProperty("pageSize")
    private Integer pageSize;
}
