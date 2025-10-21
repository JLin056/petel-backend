package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MERCH003TranrqPage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 目前頁碼
     */
    @JsonProperty("pageNumber")
    @NotNull(message = "目前頁碼不得為空")
    @Size(min = 1, message = "數值須超過一")
    private Integer pageNumber;

    /**
     * 每頁筆數
     */
    @JsonProperty("pageSize")
    @NotNull(message = "每頁筆數不得為空")
    @Size(min = 1, message = "數值須超過一")
    private Integer pageSize;
}
