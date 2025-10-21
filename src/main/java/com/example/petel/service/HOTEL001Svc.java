package com.example.petel.service;

import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InvalidInputException;

public interface HOTEL001Svc {
    /**
     * 查詢旅館列表
     * @param hotel001Tranrq 查詢條件
     * @return 旅館列表（含分頁資訊）
     * @throws DataNotFoundException 查無資料
     * @throws InvalidInputException 輸入參數錯誤
     */
    Res<HOTEL001Tranrs<HOTEL001TranrsHotel>> queryHotels(Req<HOTEL001Tranrq> hotel001Tranrq)
            throws DataNotFoundException, InvalidInputException;
}
