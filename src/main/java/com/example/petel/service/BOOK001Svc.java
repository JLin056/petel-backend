package com.example.petel.service;

import com.example.petel.dto.*;
import com.example.petel.exception.InsertFailException;

public interface BOOK001Svc {
    Res<Object> book001(Req<BOOK001Tranrq> requestBody) throws InsertFailException;
}