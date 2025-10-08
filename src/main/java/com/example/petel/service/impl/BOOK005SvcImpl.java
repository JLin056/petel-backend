package com.example.petel.service.impl;

import com.example.petel.dto.BOOK005Tranrq;
import com.example.petel.dto.BOOKTranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.service.BOOK005Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * BOOK-005 付款 SvcImpl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BOOK005SvcImpl implements BOOK005Svc {

    /**
     * 付款
     * @param requestBody Req<BOOK005Tranrq>
     * @return Res<BOOKTranrs>
     */
    @Override
    public Res<BOOKTranrs> book005(Req<BOOK005Tranrq> requestBody) throws Exception {
        return null;
    }
}

