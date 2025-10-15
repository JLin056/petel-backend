package com.example.petel.service;

import com.example.petel.dto.AUTH001Tranrq;
import com.example.petel.dto.AUTH001Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.InsertFailException;
import com.example.petel.exception.InvalidInputException;

public interface AUTH001Svc {
    Res<AUTH001Tranrs> register(Req<AUTH001Tranrq> req)
            throws InvalidInputException, InsertFailException;
}
