package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.MediaBase64Entity;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.MediaBase64Repository;
import com.example.petel.service.MEDIA004Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * MEDIA-004 Base64 圖片查詢 Service Implementation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MEDIA004SvcImpl implements MEDIA004Svc {

    private final MediaBase64Repository mediaBase64Repository;

    /**
     * 查詢 Base64 圖片資料 (支援多種查詢方式)
     * @param req Base64 圖片查詢請求
     * @return 查詢結果
     */
    @Override
    public Res<MEDIA004Tranrs> queryBase64Media(Req<MEDIA004Tranrq> req) {
        log.info("-------- [MEDIA-004] Base64 圖片查詢 ---------");

        MEDIA004Tranrq tranrq = req.getTranrq();
        List<MediaBase64Entity> mediaEntities = new ArrayList<>();

        // 1. 優先使用 mediaIds 查詢
        if (tranrq.getMediaIds() != null && !tranrq.getMediaIds().isEmpty()) {
            log.info("[MEDIA-004] 依 mediaIds 查詢，數量：{}", tranrq.getMediaIds().size());

            for (String mediaId : tranrq.getMediaIds()) {
                Optional<MediaBase64Entity> mediaOptional = mediaBase64Repository.findById(mediaId);
                mediaOptional.ifPresent(mediaEntities::add);
            }

        } else if (tranrq.getBucket() != null && !tranrq.getBucket().trim().isEmpty()) {
            // 2. 使用 bucket 查詢
            log.info("[MEDIA-004] 依 bucket 查詢：{}", tranrq.getBucket());
            mediaEntities = mediaBase64Repository.findByBucket(tranrq.getBucket());

        } else {
            // 3. 查詢全部
            log.info("[MEDIA-004] 查詢全部媒體");
            mediaEntities = mediaBase64Repository.findAll();
        }

        log.info("[MEDIA-004] 查詢到 {} 筆媒體", mediaEntities.size());

        // 4. 轉換為 DTO
        List<MEDIA004TranrsMediaInfo> mediaInfos = new ArrayList<>();
        for (MediaBase64Entity entity : mediaEntities) {
            MEDIA004TranrsMediaInfo mediaInfo = new MEDIA004TranrsMediaInfo();
            mediaInfo.setMediaId(entity.getId());
            mediaInfo.setBase64Data(entity.getBase64Data());
            mediaInfo.setBucket(entity.getBucket());
            mediaInfo.setFileName(entity.getFileName());
            mediaInfo.setSizeBytes(entity.getSizeBytes());
            mediaInfo.setMimeType(entity.getMimeType());
            mediaInfo.setCreatedAt(entity.getCreatedAt());
            mediaInfo.setUpdatedAt(entity.getUpdatedAt());

            mediaInfos.add(mediaInfo);
        }

        // 5. 建立回應
        MEDIA004Tranrs tranrs = new MEDIA004Tranrs(
                mediaInfos.size(),
                mediaInfos
        );

        return new Res<>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                tranrs
        );
    }
}
