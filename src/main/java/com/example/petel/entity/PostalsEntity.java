package com.example.petel.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PETEL_POSTALS")
public class PostalsEntity {

    /**
     * 郵遞區號 ID
     */
    @Id
    @Column(name = "ID")
    @JsonProperty("id")
    private String id;

    /**
     * 縣市
     */
    @Column(name = "CITY")
    @JsonProperty("city")
    private String city;

    /**
     * 區域
     */
    @Column(name = "DISTRICT")
    @JsonProperty("district")
    private String district;
}
