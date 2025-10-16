package com.example.petel.service;

import com.example.petel.dto.IMG001Tranrq;
import com.example.petel.dto.IMG001Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.InsertFailException;

/**
 * IMG-001 圖片上傳 Service
 */
public interface IMG001Svc {

    /**
     * 上傳圖片到 S3 並儲存到資料庫
     * @param req 圖片上傳請求
     * @return 上傳結果
     * @throws InsertFailException 上傳失敗異常
     */
    Res<IMG001Tranrs> uploadImage(Req<IMG001Tranrq> req) throws InsertFailException;
}