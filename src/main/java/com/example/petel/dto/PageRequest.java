package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 共用分頁請求參數
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 頁碼
     */
    @JsonProperty("pageNumber")
    @NotNull(message = "頁碼不得為空")
    @Min(value = 1, message = "頁碼須大於等於 1")
    private Integer pageNumber;

    /**
     * 每頁筆數
     */
    @JsonProperty("pageSize")
    @NotNull(message = "每頁筆數不得為空")
    @Min(value = 1, message = "每頁筆數須大於等於 1")
    private Integer pageSize;
}
