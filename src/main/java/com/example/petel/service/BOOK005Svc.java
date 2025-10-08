package com.example.petel.service;

import com.example.petel.dto.BOOK005Tranrq;
import com.example.petel.dto.BOOKTranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;

public interface BOOK005Svc {
    Res<BOOKTranrs> book005(Req<BOOK005Tranrq> requestBody) throws Exception;
}

