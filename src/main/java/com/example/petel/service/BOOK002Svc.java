package com.example.petel.service;

import com.example.petel.dto.BOOK002Tranrq;
import com.example.petel.dto.BOOKTranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;

import java.io.IOException;

public interface BOOK002Svc {
    Res<BOOKTranrs> book002(Req<BOOK002Tranrq> requestBody) throws DataNotFoundException, IOException;
}

