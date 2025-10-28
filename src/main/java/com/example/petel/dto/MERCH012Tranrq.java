package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MERCH012Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * room id
     */
    @JsonProperty("id")
    @NotBlank(message = "id不得為空")
    private String id;
}
