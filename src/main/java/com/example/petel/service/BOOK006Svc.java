package com.example.petel.service;

import com.example.petel.dto.BOOK006Tranrq;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;

public interface BOOK006Svc {
    Res<Object> book006(Req<BOOK006Tranrq> requestBody) throws Exception;
}
