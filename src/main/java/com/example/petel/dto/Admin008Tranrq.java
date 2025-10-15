package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin008Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 使用者 ID (必填)
     */
    @NotNull(message = "使用者 ID 不得為空")
    @JsonProperty("usersId")
    private String usersId;
}
