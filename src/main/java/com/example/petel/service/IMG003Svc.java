package com.example.petel.service;

import com.example.petel.dto.IMG003Tranrq;
import com.example.petel.dto.IMG003Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DeleteFailException;

/**
 * IMG-003 圖片刪除 Service
 */
public interface IMG003Svc {

    /**
     * 刪除圖片 (刪除 S3 檔案並移除資料庫記錄)
     * @param req 圖片刪除請求
     * @return 刪除結果
     * @throws DeleteFailException 刪除失敗異常
     */
    Res<IMG003Tranrs> deleteImage(Req<IMG003Tranrq> req) throws DeleteFailException;
}
