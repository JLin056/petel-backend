package com.example.petel.service;

import com.example.petel.dto.BOOK008Tranrq;
import com.example.petel.dto.BOOK008Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;

public interface BOOK008Svc {
    Res<BOOK008Tranrs> book008(Req<BOOK008Tranrq> requestBody) throws Exception;
}