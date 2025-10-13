package com.example.petel.service;

import com.example.petel.dto.BOOK009Tranrq;
import com.example.petel.dto.BOOK009Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;

public interface BOOK009Svc {
    Res<BOOK009Tranrs> book009(Req<BOOK009Tranrq> requestBody) throws Exception;
}