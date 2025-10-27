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
public class HOTEL005TranrsReview implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 價格評分
     */
    @JsonProperty("priceScore")
    private Integer priceScore;

    /**
     * 環境評分
     */
    @JsonProperty("envScore")
    private Integer envScore;

    /**
     * 服務評分
     */
    @JsonProperty("serviceScore")
    private Integer serviceScore;

    /**
     * 評價內容
     */
    @JsonProperty("content")
    private String content;
}
