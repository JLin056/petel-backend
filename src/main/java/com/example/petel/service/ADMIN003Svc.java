package com.example.petel.service;

import com.example.petel.dto.ADMIN003Tranrq;
import com.example.petel.dto.ADMIN003Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;

import java.io.IOException;

public interface ADMIN003Svc {

    /**
     * 查詢訂單列表
     * @param req Req<ADMIN003Tranrq>
     * @return Res<ADMIN003Tranrs>
     * @throws DataNotFoundException 查無資料
     */
    Res<ADMIN003Tranrs> queryOrders(Req<ADMIN003Tranrq> req) throws DataNotFoundException, IOException;
}
