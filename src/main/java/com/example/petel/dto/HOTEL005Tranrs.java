package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class HOTEL005Tranrs implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("propertyNotice")
    private String propertyNotice;
}