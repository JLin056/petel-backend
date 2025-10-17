package com.example.petel.service;

import com.example.petel.dto.IMG002Tranrq;
import com.example.petel.dto.IMG002Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.UpdateFailException;

/**
 * IMG-002 圖片更新 Service
 */
public interface IMG002Svc {

    /**
     * 更新圖片 (替換 S3 檔案並更新資料庫)
     * @param req 圖片更新請求
     * @return 更新結果
     * @throws UpdateFailException 更新失敗異常
     */
    Res<IMG002Tranrs> updateImage(Req<IMG002Tranrq> req) throws UpdateFailException;
}
