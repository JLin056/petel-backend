package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class USER006Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * status
     */
    @JsonProperty("status")
    private String status;

    /**
     * from
     */
    @JsonProperty("from")
    private String from;

    /**
     * to
     */
    @JsonProperty("to")
    private String to;

    /**
     * page
     */
    @JsonProperty("page")
    private Integer page;

    /**
     * pageSize
     */
    @JsonProperty("pageSize")
    private Integer pageSize;

}
