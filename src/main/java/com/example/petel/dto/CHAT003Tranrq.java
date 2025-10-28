package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CHAT003Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 聊天室 Id
     */
    @JsonProperty("threadId")
    @NotBlank(message = "threadId 不得為空")
    private String threadId;

    /**
     * pageSize
     */
    @JsonProperty("pageSize")
    private Integer pageSize;

    /**
     * pageNumber
     */
    @JsonProperty("pageNumber")
    private Integer pageNumber;
}
