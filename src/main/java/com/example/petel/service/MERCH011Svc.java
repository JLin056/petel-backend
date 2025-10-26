package com.example.petel.service;

import com.example.petel.dto.MERCH011Tranrs;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;

public interface MERCH011Svc {
    Res<MERCH011Tranrs> getSellerInfo(String accountId) throws DataNotFoundException;
}
