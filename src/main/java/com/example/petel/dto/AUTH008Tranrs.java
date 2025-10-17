package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AUTH008Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * valid
     */
    private boolean valid;
}
