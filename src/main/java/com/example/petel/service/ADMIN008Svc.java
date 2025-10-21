package com.example.petel.service;

import com.example.petel.dto.ADMIN008Tranrq;
import com.example.petel.dto.ADMIN008Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.DeleteFailException;

public interface ADMIN008Svc {

    /**
     * 刪除使用者（連同其關聯的帳號）
     * @param req Req<Admin008Tranrq>
     * @return Res<Admin008Tranrs>
     * @throws DataNotFoundException 使用者不存在
     * @throws DeleteFailException 刪除失敗
     */
    Res<ADMIN008Tranrs> deleteUser(Req<ADMIN008Tranrq> req) throws DataNotFoundException, DeleteFailException;
}
