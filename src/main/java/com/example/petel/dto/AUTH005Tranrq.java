package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AUTH005Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * email
     */
    @JsonProperty("token")
    @NotBlank(message = "token 不得為空")
    private String token;

    /**
     * newPassword
     */
    @JsonProperty("newPassword")
    @NotBlank(message = "newPassword 不得為空")
    @Size(min = 6, max = 64, message = "密碼長度需介於 6~64")
    private String newPassword;
}
