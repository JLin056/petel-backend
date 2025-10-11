package com.example.petel.service;

import com.example.petel.dto.BOOK003Tranrq;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.UpdateFailException;

public interface BOOK003Svc {
    Res<Object> book003(Req<BOOK003Tranrq> requestBody) throws DataNotFoundException, UpdateFailException;
}