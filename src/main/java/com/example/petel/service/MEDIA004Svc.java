package com.example.petel.service;

import com.example.petel.dto.MEDIA004Tranrq;
import com.example.petel.dto.MEDIA004Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;

/**
 * MEDIA-004 Base64 圖片查詢 Service
 */
public interface MEDIA004Svc {

    /**
     * 查詢 Base64 圖片資料 (支援多種查詢方式)
     * @param req Base64 圖片查詢請求
     * @return 查詢結果
     */
    Res<MEDIA004Tranrs> queryBase64Media(Req<MEDIA004Tranrq> req);
}
