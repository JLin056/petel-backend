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
public class BOOK007TranrsRqHeader implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("Timestamp")
    private long timestamp;
}