package com.example.petel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MERCH015Tranrq implements Serializable {
    private String city; // 可選，用來過濾
}
