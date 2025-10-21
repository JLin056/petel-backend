package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MERCH001Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * property id
     */
    @JsonProperty("propertyId")
    @NotBlank(message = "propertyId不得為空")
    private String propertyId;

    /**
     * 入住日期
     */
    @Column(name = "arrivalDate")
    private String arrivalDate;

    /**
     * 頁碼資料
     */
    @JsonProperty("page")
    private PageRequest page;
}
