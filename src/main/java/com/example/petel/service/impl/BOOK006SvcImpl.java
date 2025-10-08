package com.example.petel.service.impl;

import com.example.petel.dto.BOOK006Tranrq;
import com.example.petel.dto.BOOKTranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.service.BOOK006Svc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BOOK006SvcImpl implements BOOK006Svc {
    @Override
    public Res<BOOKTranrs> book006(Req<BOOK006Tranrq> requestBody) throws Exception {
        return null;
    }
}