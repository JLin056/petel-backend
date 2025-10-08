package com.example.petel.service.impl;

import com.example.petel.dto.BOOK005Tranrq;
import com.example.petel.dto.BOOKTranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.service.BOOK005Svc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BOOK005SvcImpl implements BOOK005Svc {
    @Override
    public Res<BOOKTranrs> book005(Req<BOOK005Tranrq> requestBody) throws Exception {
        return null;
    }
}

