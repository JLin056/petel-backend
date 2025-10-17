package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BOOK005TranrqConsumerInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * phone
     */
    @JsonProperty("phone")
    @NotNull(message = "信用卡持卡人電話必填")
    private String phone;

    /**
     * name
     */
    @JsonProperty("name")
    @NotNull(message = "信用卡持卡人英文姓名必填")
    private String name;
}