package com.example.petel.service;

import com.example.petel.dto.MERCH007Tranrq;
import com.example.petel.dto.MERCH007Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.UpdateFailException;
import jakarta.validation.Valid;

public interface MERCH007Svc {
    Res<MERCH007Tranrs> update(@Valid Req<MERCH007Tranrq> merch007Tranrq) throws DataNotFoundException, UpdateFailException;
}
