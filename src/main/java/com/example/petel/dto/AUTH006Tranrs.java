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
public class AUTH006Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * account ID
     */
    @JsonProperty("AccountId")
    private String accountId;

    /**
     * Email
     */
    @JsonProperty("Email")
    private String email;

    /**
     * Role
     */
    @JsonProperty("Role")
    private String role;

    /**
     * 若為 user 則為 userID，若為 seller 則為 sellerID
     */
    @JsonProperty("mainId")
    private String mainId;

}
