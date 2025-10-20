package com.example.petel.service;

import com.example.petel.dto.MERCH006Tranrq;
import com.example.petel.dto.MERCH006Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.DeleteFailException;
import jakarta.validation.Valid;

public interface MERCH006Svc {
    Res<MERCH006Tranrs> delete(@Valid Req<MERCH006Tranrq> merch005Tranrq) throws DataNotFoundException, DeleteFailException;
}

