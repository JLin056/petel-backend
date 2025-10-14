package com.example.petel.service;

import com.example.petel.dto.Admin002Tranrq;
import com.example.petel.dto.Admin002Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;

public interface Admin002Svc {

    /**
     * 查詢賣家列表
     * @param req Req<Admin002Tranrq>
     * @return Res<Admin002Tranrs>
     * @throws DataNotFoundException 查無資料
     */
    Res<Admin002Tranrs> querySellers(Req<Admin002Tranrq> req) throws Exception, DataNotFoundException;
}
