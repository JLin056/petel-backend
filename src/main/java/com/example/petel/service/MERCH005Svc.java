package com.example.petel.service;

import com.example.petel.dto.MERCH005Tranrq;
import com.example.petel.dto.MERCH005Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.UpdateFailException;
import jakarta.validation.Valid;

public interface MERCH005Svc {
    Res<MERCH005Tranrs> edit(@Valid Req<MERCH005Tranrq> merch005Tranrq) throws DataNotFoundException, UpdateFailException;
}
