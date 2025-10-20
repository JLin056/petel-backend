package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HOTEL001TranrqPage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Page number (0-based)
     */
    @JsonProperty("pageNumber")
    @Min(value = 0, message = "pageNumber must be >= 0")
    private Integer pageNumber;

    /**
     * Page size
     */
    @JsonProperty("pageSize")
    @Min(value = 1, message = "pageSize must be >= 1")
    private Integer pageSize;
}
