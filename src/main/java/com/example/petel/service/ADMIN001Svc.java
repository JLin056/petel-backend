package com.example.petel.service;

import com.example.petel.dto.ADMIN001Tranrq;
import com.example.petel.dto.ADMIN001Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;

import java.io.IOException;

public interface ADMIN001Svc {

    /**
     * 查詢所有旅館列表
     * @param req Req<ADMIN001Tranrq>
     * @return Res<ADMIN001Tranrs>
     * @throws DataNotFoundException 查無資料
     * @throws IOException SQL 檔案讀取錯誤
     */
    Res<ADMIN001Tranrs> queryStores(Req<ADMIN001Tranrq> req) throws DataNotFoundException, IOException;
}
