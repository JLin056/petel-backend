package com.example.petel.service;

import com.example.petel.dto.MEDIA003Tranrq;
import com.example.petel.dto.MEDIA003Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;

/**
 * MEDIA-003 Base64 圖片刪除 Service
 */
public interface MEDIA003Svc {

    /**
     * 刪除 Base64 圖片資料 (支援批量)
     * @param req Base64 圖片刪除請求
     * @return 刪除結果
     */
    Res<MEDIA003Tranrs> deleteBase64Media(Req<MEDIA003Tranrq> req);
}
