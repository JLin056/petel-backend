package com.example.petel.service;

import com.example.petel.dto.MERCH004Tranrq;
import com.example.petel.dto.MERCH004Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.InsertFailException;
import jakarta.validation.Valid;

public interface MERCH004Svc {
    Res<MERCH004Tranrs> create(@Valid Req<MERCH004Tranrq> merch004Tranrq) throws InsertFailException;
}
