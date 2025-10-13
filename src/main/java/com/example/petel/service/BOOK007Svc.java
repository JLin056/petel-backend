package com.example.petel.service;

import com.example.petel.dto.BOOK007Tranrq;
import com.example.petel.dto.BOOK007Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;

public interface BOOK007Svc {
    Res<BOOK007Tranrs> book007(Req<BOOK007Tranrq> requestBody) throws Exception;
}