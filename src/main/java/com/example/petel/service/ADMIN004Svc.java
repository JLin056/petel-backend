package com.example.petel.service;

import com.example.petel.dto.ADMIN004Tranrq;
import com.example.petel.dto.ADMIN004Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.UpdateFailException;

import java.io.IOException;

public interface ADMIN004Svc {

    /**
     * 更新訂單備註
     * @param req Req<ADMIN004Tranrq>
     * @return Res<ADMIN004Tranrs>
     * @throws DataNotFoundException 訂單不存在
     * @throws UpdateFailException 更新失敗
     */
    Res<ADMIN004Tranrs> updateOrderNote(Req<ADMIN004Tranrq> req) throws DataNotFoundException, UpdateFailException, IOException;
}
