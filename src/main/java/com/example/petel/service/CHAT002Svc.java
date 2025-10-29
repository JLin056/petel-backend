package com.example.petel.service;

import com.example.petel.dto.CHAT002Tranrq;
import com.example.petel.dto.CHAT002Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.model.jwt.AccountPrincipal;

import java.io.IOException;

public interface CHAT002Svc {
    Res<CHAT002Tranrs> getThreads(AccountPrincipal auth, Req<CHAT002Tranrq> req)
            throws IOException;
}
