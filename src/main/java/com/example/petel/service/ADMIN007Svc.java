package com.example.petel.service;

import com.example.petel.dto.ADMIN007Tranrq;
import com.example.petel.dto.ADMIN007Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;

import java.io.IOException;

public interface ADMIN007Svc {

    /**
     * 查詢會員列表
     * @param req Req<ADMIN007Tranrq>
     * @return Res<ADMIN007Tranrs>
     * @throws DataNotFoundException 查無資料
     */
    Res<ADMIN007Tranrs> queryMembers(Req<ADMIN007Tranrq> req) throws DataNotFoundException, IOException;
}
