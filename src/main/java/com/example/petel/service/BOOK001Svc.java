package com.example.petel.service;

import com.example.petel.dto.*;
import com.example.petel.exception.InsertFailException;

public interface BOOK001Svc {
    Res<BOOK001Tranrs> book001(Req<BOOK001Tranrq> requestBody) throws InsertFailException;
}