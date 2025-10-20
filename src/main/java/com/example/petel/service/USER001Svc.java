package com.example.petel.service;

import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.dto.USER001Tranrq;
import com.example.petel.dto.USER001Tranrs;
import com.example.petel.exception.InsertFailException;

public interface USER001Svc {

    Res<USER001Tranrs> createUser(String accountId, Req<USER001Tranrq> req)
            throws InsertFailException;
}
