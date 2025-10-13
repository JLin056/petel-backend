package com.example.petel.service;

import com.example.petel.dto.Admin001Tranrq;
import com.example.petel.dto.Admin001Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;

public interface Admin001Svc {

    /**
     * 查詢所有旅館列表
     * @param req Req<Admin001Tranrq>
     * @return Res<Admin001Tranrs>
     * @throws DataNotFoundException 查無資料
     */
    Res<Admin001Tranrs> queryStores(Req<Admin001Tranrq> req) throws Exception, DataNotFoundException;
}
