package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class HOTEL002Tranrs<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * property  details
     */
    @JsonProperty("property_details")
    private List<T> details;
}