package com.example.petel.service;

import com.example.petel.dto.BOOK004Tranrq;
import com.example.petel.dto.BOOKTranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.DeleteFailException;

public interface BOOK004Svc {
    Res<BOOKTranrs> book004(Req<BOOK004Tranrq> requestBody) throws DataNotFoundException, DeleteFailException;
}

