package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.MediaBase64Entity;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.MediaBase64Repository;
import com.example.petel.service.MEDIA003Svc;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * MEDIA-003 Base64 圖片刪除 Service Implementation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MEDIA003SvcImpl implements MEDIA003Svc {

    private final MediaBase64Repository mediaBase64Repository;

    /**
     * 刪除 Base64 圖片資料 (支援批量)
     * @param req Base64 圖片刪除請求
     * @return 刪除結果
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<MEDIA003Tranrs> deleteBase64Media(Req<MEDIA003Tranrq> req) {
        log.info("-------- [MEDIA-003] Base64 圖片刪除 ---------");

        MEDIA003Tranrq tranrq = req.getTranrq();
        log.info("[MEDIA-003] 媒體刪除數量：{}", tranrq.getMediaIds().size());

        List<MEDIA003TranrsMediaResult> results = new ArrayList<>();
        int successCount = 0;
        int failedCount = 0;

        // 處理每個媒體刪除
        for (int i = 0; i < tranrq.getMediaIds().size(); i++) {
            String mediaId = tranrq.getMediaIds().get(i);
            log.info("[MEDIA-003] 處理媒體 [{}]：ID={}", i, mediaId);

            try {
                // 1. 驗證媒體ID不能為空
                if (mediaId == null || mediaId.trim().isEmpty()) {
                    throw new IllegalArgumentException("媒體ID不得為空");
                }

                // 2. 查詢媒體是否存在
                Optional<MediaBase64Entity> mediaOptional = mediaBase64Repository.findById(mediaId);
                if (mediaOptional.isEmpty()) {
                    throw new IllegalArgumentException("媒體ID不存在：" + mediaId);
                }

                MediaBase64Entity existingMedia = mediaOptional.get();
                log.info("[MEDIA-003] 找到媒體記錄：{}", existingMedia.getFileName());

                // 3. 執行刪除
                mediaBase64Repository.delete(existingMedia);
                log.info("[MEDIA-003] 媒體刪除成功，ID={}", mediaId);

                // 4. 添加成功結果
                MEDIA003TranrsMediaResult result = new MEDIA003TranrsMediaResult();
                result.setMediaId(mediaId);
                result.setStatus("SUCCESS");
                result.setErrorMessage(null);

                results.add(result);
                successCount++;

            } catch (Exception e) {
                log.error("[MEDIA-003] 媒體 [{}] 刪除失敗：{}", i, e.getMessage(), e);

                // 添加失敗結果
                MEDIA003TranrsMediaResult result = new MEDIA003TranrsMediaResult();
                result.setMediaId(mediaId);
                result.setStatus("FAILED");
                result.setErrorMessage(e.getMessage());

                results.add(result);
                failedCount++;
            }
        }

        log.info("[MEDIA-003] 批量刪除完成，成功：{}，失敗：{}", successCount, failedCount);

        // 5. 建立回應
        MEDIA003Tranrs tranrs = new MEDIA003Tranrs(
                successCount,
                failedCount,
                results
        );

        return new Res<>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                tranrs
        );
    }
}
