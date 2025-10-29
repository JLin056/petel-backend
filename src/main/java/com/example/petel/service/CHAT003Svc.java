package com.example.petel.service;

import com.example.petel.dto.CHAT003Tranrq;
import com.example.petel.dto.CHAT003Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.model.jwt.AccountPrincipal;

import java.io.IOException;

public interface CHAT003Svc {

    Res<CHAT003Tranrs> getRoom(AccountPrincipal auth, Req<CHAT003Tranrq> req) throws DataNotFoundException, IOException;
}
