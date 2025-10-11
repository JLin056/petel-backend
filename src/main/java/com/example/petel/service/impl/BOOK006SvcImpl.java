package com.example.petel.service.impl;

import com.example.petel.dto.BOOK006Tranrq;
import com.example.petel.dto.BOOK002Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.service.BOOK006Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * BOOK-006 退款 SvcImpl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BOOK006SvcImpl implements BOOK006Svc {

    /**
     * 退款
     * @param requestBody Req<BOOK006Tranrq>
     * @return Res<Object>
     */
    @Override
    public Res<Object> book006(Req<BOOK006Tranrq> requestBody) throws Exception {
        return null;
    }
}