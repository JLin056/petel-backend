package com.example.petel.service;

import com.example.petel.dto.ADMIN005Tranrq;
import com.example.petel.dto.ADMIN005Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.UpdateFailException;

public interface ADMIN005Svc {

    /**
     * 取消訂單
     * @param req Req<ADMIN005Tranrq>
     * @return Res<ADMIN005Tranrs>
     * @throws DataNotFoundException 訂單不存在
     * @throws UpdateFailException 取消失敗
     */
    Res<ADMIN005Tranrs> cancelOrder(Req<ADMIN005Tranrq> req) throws DataNotFoundException, UpdateFailException;
}
