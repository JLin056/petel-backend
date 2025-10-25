package com.example.petel.service;

import com.example.petel.dto.MEDIA002Tranrq;
import com.example.petel.dto.MEDIA002Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;

/**
 * MEDIA-002 Base64 圖片更新 Service
 */
public interface MEDIA002Svc {

    /**
     * 更新 Base64 圖片資料 (支援批量)
     * @param req Base64 圖片更新請求
     * @return 更新結果
     */
    Res<MEDIA002Tranrs> updateBase64Media(Req<MEDIA002Tranrq> req);
}
