package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HOTEL001TranrqPage implements Serializable {

    /**
     * 頁碼（從1開始）
     */
    @JsonProperty("pageNumber")
    @Min(value = 1, message = "pageNumber must be >= 1")
    private Integer pageNumber;

    /**
     * 每頁筆數
     */
    @JsonProperty("pageSize")
    @Min(value = 1, message = "pageSize must be >= 1")
    private Integer pageSize;
}
