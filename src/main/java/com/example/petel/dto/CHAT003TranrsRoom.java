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
public class CHAT003TranrsRoom implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * threadId
     */
    @JsonProperty("threadId")
    private String threadId;

    /**
     * role
     */
    @JsonProperty("role")
    private String role;

    /**
     * buyerAccountId
     */
    @JsonProperty("buyerAccountId")
    private String buyerAccountId;

    /**
     * sellerAccountId
     */
    @JsonProperty("sellerAccountId")
    private String sellerAccountId;

    /**
     * propertyId
     */
    @JsonProperty("propertyId")
    private String propertyId;

    /**
     * displayName
     */
    @JsonProperty("displayName")
    private String displayName;

    /**
     * orderId
     */
    @JsonProperty("orderId")
    private String orderId;

    /**
     * orderStatus
     */
    @JsonProperty("orderStatus")
    private String orderStatus;
}
