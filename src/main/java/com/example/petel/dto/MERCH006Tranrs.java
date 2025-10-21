package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MERCH006Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
}
