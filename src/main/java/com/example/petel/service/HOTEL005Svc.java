package com.example.petel.service;

import com.example.petel.dto.HOTEL005Tranrq;
import com.example.petel.dto.HOTEL005Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;

public interface HOTEL005Svc {
    Res<HOTEL005Tranrs> policies(Req<HOTEL005Tranrq> hotel005Tranrq) throws DataNotFoundException;
}
