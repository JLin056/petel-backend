package com.example.petel.service;

import com.example.petel.dto.ADMIN002Tranrq;
import com.example.petel.dto.ADMIN002Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;

import java.io.IOException;

public interface ADMIN002Svc {

    /**
     * 查詢賣家列表
     *
     * @param req Req<ADMIN002Tranrq>
     * @return Res<ADMIN002Tranrs>
     * @throws DataNotFoundException 查無資料
     * @throws IOException           SQL 檔案讀取錯誤
     */
    Res<ADMIN002Tranrs> querySellers(Req<ADMIN002Tranrq> req) throws DataNotFoundException, IOException;
}
