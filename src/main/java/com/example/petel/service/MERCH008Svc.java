package com.example.petel.service;

import com.example.petel.dto.MERCH008Tranrq;
import com.example.petel.dto.MERCH008Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InsertFailException;
import jakarta.validation.Valid;

public interface MERCH008Svc {
    Res<MERCH008Tranrs> insert(@Valid Req<MERCH008Tranrq> merch008Tranrq) throws DataNotFoundException, InsertFailException;
}
