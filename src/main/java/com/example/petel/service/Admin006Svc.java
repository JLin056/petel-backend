package com.example.petel.service;

import com.example.petel.dto.Admin006Tranrq;
import com.example.petel.dto.Admin006Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.DeleteFailException;

public interface Admin006Svc {

    /**
     * 刪除旅館
     * @param req Req<Admin006Tranrq>
     * @return Res<Admin006Tranrs>
     * @throws DataNotFoundException 旅館不存在
     * @throws DeleteFailException 刪除失敗
     */
    Res<Admin006Tranrs> deleteHotel(Req<Admin006Tranrq> req) throws Exception, DataNotFoundException, DeleteFailException;
}
