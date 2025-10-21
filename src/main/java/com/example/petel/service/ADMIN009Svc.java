package com.example.petel.service;

import com.example.petel.dto.ADMIN009Tranrq;
import com.example.petel.dto.ADMIN009Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.DeleteFailException;

public interface ADMIN009Svc {

    /**
     * 刪除賣家（連同其相關的所有物業和帳號）
     * @param req Req<Admin009Tranrq>
     * @return Res<Admin009Tranrs>
     * @throws DataNotFoundException 賣家不存在
     * @throws DeleteFailException 刪除失敗
     */
    Res<ADMIN009Tranrs> deleteSeller(Req<ADMIN009Tranrq> req) throws DataNotFoundException, DeleteFailException;
}
