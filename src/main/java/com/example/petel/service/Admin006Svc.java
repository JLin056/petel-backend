package com.example.petel.service;

import com.example.petel.dto.ADMIN006Tranrq;
import com.example.petel.dto.ADMIN006Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.DeleteFailException;

public interface Admin006Svc {

    /**
     * 刪除旅館
     * @param req Req<ADMIN006Tranrq>
     * @return Res<ADMIN006Tranrs>
     * @throws DataNotFoundException 旅館不存在
     * @throws DeleteFailException 刪除失敗
     */
    Res<ADMIN006Tranrs> deleteHotel(Req<ADMIN006Tranrq> req) throws DataNotFoundException, DeleteFailException;
}
