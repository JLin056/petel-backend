package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class USER002Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * name
     */
    @JsonProperty("name")
    private String name;

    /**
     * phone
     */
    @JsonProperty("phone")
    @Pattern(regexp = "^[0-9+\\-()\\s]{6,20}$", message = "phone 格式不正確")
    private String phone;

    /**
     * mediaId
     */
    @JsonProperty("mediaId")
    private String mediaId;
}
