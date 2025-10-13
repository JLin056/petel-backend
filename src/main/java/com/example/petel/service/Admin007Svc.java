package com.example.petel.service;

import com.example.petel.dto.Admin007Tranrq;
import com.example.petel.dto.Admin007Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;

public interface Admin007Svc {

    /**
     * 查詢會員列表
     * @param req Req<Admin007Tranrq>
     * @return Res<Admin007Tranrs>
     * @throws DataNotFoundException 查無資料
     */
    Res<Admin007Tranrs> queryMembers(Req<Admin007Tranrq> req) throws Exception, DataNotFoundException;
}
