package com.example.petel.service;

import com.example.petel.dto.MERCH012Tranrq;
import com.example.petel.dto.MERCH012Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;
import jakarta.validation.Valid;

public interface MERCH012Svc {
    Res<MERCH012Tranrs> getRoomInfo(@Valid Req<MERCH012Tranrq> merch012Tranrq) throws DataNotFoundException;
}
