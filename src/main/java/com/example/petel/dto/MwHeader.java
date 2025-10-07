package com.example.petel.dto;

import com.example.petel.model.ReturnCodeAndDescEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MwHeader implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;



    /**
     * 處理結果代碼
     */
    @JsonProperty("RETURNCODE")
    private String returnCode;

    /**
     * 處理結果訊息
     */
    @JsonProperty("RETURNDESC")
    private String returnDesc;

    /**
     * 建構子
     *
     * @param returnCode
     */
    public MwHeader(ReturnCodeAndDescEnum returnCode) {
        this.returnCode = returnCode.getCode();
        this.returnDesc = returnCode.getDesc();
    }


}
