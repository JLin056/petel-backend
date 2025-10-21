package com.example.petel.service;

import com.example.petel.dto.CHAT001Tranrq;
import com.example.petel.dto.CHAT001Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InsertFailException;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.model.jwt.AccountPrincipal;

public interface CHAT001Svc {

    Res<CHAT001Tranrs> GetOrCreateByOrder(Req<CHAT001Tranrq> req, AccountPrincipal auth)
            throws DataNotFoundException, InvalidInputException, InsertFailException;
}
