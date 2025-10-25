package com.example.petel.service;

import com.example.petel.dto.MEDIA001Tranrq;
import com.example.petel.dto.MEDIA001Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.InsertFailException;

/**
 * MEDIA-001 Base64 圖片上傳 Service (儲存到資料庫)
 */
public interface MEDIA001Svc {

    /**
     * 上傳 Base64 圖片並儲存到資料庫
     * @param req Base64 圖片上傳請求
     * @return 上傳結果
     * @throws InsertFailException 上傳失敗異常
     */
    Res<MEDIA001Tranrs> uploadBase64Media(Req<MEDIA001Tranrq> req) throws InsertFailException;
}
