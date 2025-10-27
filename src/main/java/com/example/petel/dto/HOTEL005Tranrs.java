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
public class HOTEL005Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 單筆旅館詳細資訊
     */
    @JsonProperty("singleHotelDetail")
    private HOTEL005TranrsSingleHotelDetail singleHotelDetail;
}
