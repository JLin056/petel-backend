package com.example.petel.service;

import com.example.petel.dto.BOOK001Tranrq;
import com.example.petel.dto.BOOKTranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;

public interface BOOK001Svc {
    Res<BOOKTranrs> book001(Req<BOOK001Tranrq> requestBody) throws Exception;
}