package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.ReviewEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.ReviewRepository;
import com.example.petel.service.MERCH003Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MERCH003SvcImpl implements MERCH003Svc {

    /**
     * ReviewRepository
     */
    private final ReviewRepository reviewRepository;

    /**
     * 評價列表
     *
     * @param merch003Tranrq Req<MERCH003Tranrq> （propertyId)
     * @return Res<MERCH003Tranrs>
     * @throws DataNotFoundException
     */
    @Override
    public Res<MERCH003Tranrs<MERCH003TranrsReview>> reviews(Req<MERCH003Tranrq> merch003Tranrq) throws DataNotFoundException {
        log.info("-------- [MERCH-003] 評價列表 ---------");
        Long propertyId = merch003Tranrq.getTranrq().getPropertyId();
        log.info("[MERCH-003] 查詢 propertyId = {}", propertyId);

        List<ReviewEntity> reviews = reviewRepository.findByPropertyId(propertyId);
        if (reviews.isEmpty()) {
            log.warn("[MERCH-003] 依據 propertyId 查無資料");
            throw new DataNotFoundException("查無評價資料");
        }

        List<MERCH003TranrsReview> reviewList = new ArrayList<>();

        for (ReviewEntity reviewEntity : reviews) {
            MERCH003TranrsReview review = new MERCH003TranrsReview();
            review.setOrderId(reviewEntity.getOrderId());
            review.setUserId(reviewEntity.getUserId());
            review.setPriceScore(reviewEntity.getPriceScore());
            review.setEnvScore(reviewEntity.getEnvScore());
            review.setServiceScore(reviewEntity.getServiceScore());
            review.setContent(reviewEntity.getContent());
            review.setCreatedAt(reviewEntity.getCreatedAt());
            reviewList.add(review);
        }
        log.info("[MERCH-003] 查詢成功，propertyId={}, reviewList={}", propertyId, reviewList);

        MERCH003Tranrs<MERCH003TranrsReview> merch003Tranrs = new MERCH003Tranrs<>();
        merch003Tranrs.setReview(reviewList);

        return new Res<>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                merch003Tranrs
        );
    }
}
