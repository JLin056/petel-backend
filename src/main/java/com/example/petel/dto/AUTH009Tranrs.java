package com.example.petel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AUTH009Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * filled
     */
    private boolean filled;
}
