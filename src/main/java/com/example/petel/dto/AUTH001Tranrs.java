package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AUTH001Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * account ID
     */
    @JsonProperty("accountId")
    private long accountId;
}
