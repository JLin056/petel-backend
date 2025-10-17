package com.example.petel.service;

import com.example.petel.dto.MERCH009Tranrq;
import com.example.petel.dto.MERCH009Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.InsertFailException;
import jakarta.validation.Valid;

public interface MERCH009Svc {
    Res<MERCH009Tranrs> createSeller(@Valid Req<MERCH009Tranrq> merch009Tranrq) throws InsertFailException;
}
