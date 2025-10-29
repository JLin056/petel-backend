package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CHAT003Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * room
     */
    @JsonProperty("room")
    private CHAT003TranrsRoom room;

    /**
     * messages
     */
    @JsonProperty("messages")
    private List<CHAT003TranrsMessages> messages;

}
