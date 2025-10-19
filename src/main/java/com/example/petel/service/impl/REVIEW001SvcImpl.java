package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.ReviewsEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InsertFailException;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.model.IdUtil;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.OrdersRepository;
import com.example.petel.repository.ReviewsRepository;
import com.example.petel.repository.UsersRepository;
import com.example.petel.service.REVIEW001Svc;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class REVIEW001SvcImpl implements REVIEW001Svc {

    /** UsersRepository */
    private final UsersRepository usersRepo;
    /** UsersRepository */
    private final OrdersRepository ordersRepo;
    /** ReviewsRepository */
    private final ReviewsRepository reviewsRepo;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<REVIEW001Tranrs> createReview(String accountId, Req<REVIEW001Tranrq> req)
            throws DataNotFoundException, InvalidInputException, InsertFailException {
        log.info("---- [REVIEW-001] 新增評論 ----");

        REVIEW001Tranrq tranrq = req.getTranrq();
        String orderId = tranrq.getOrderId();
        // 查訂單
        log.info("[REVIEW001] 查詢訂單資料...");
        Map<String, Object> orderData = ordersRepo.findUserAndPropertyByOrderId(orderId);
        if (orderData == null) {
            log.warn("[REVIEW001] 查無該筆訂單，orderId = {}", orderId);
            throw new DataNotFoundException("查無該筆訂單");
        }

        String userId = (String) orderData.get("USERID");
        String propertyId = (String) orderData.get("PROPERTYID");
        log.info("[REVIEW001] 訂單查詢成功，userId = {}, propertyId = {}", userId, propertyId);

        // 驗證身份（開發階段先註解掉）
        String usersRepoIdByAccountId = usersRepo.findIdByAccountId(accountId);
        if (!userId.equals(usersRepoIdByAccountId)) {
            log.warn("[REVIEW001] 使用者不符，userId = {}, usersRepoIdByAccountId = {}", userId, usersRepoIdByAccountId);
            throw new InvalidInputException("無權限評論該訂單");
        }
        log.info("[REVIEW001] 身分驗證通過");

        String maxId = reviewsRepo.findMaxId();
        String reviewId = IdUtil.generateTableId("R", maxId);

        ReviewsEntity entity = new ReviewsEntity();
        entity.setId(reviewId);
        entity.setOrderId(orderId);
        entity.setUserId(userId);
        entity.setPropertyId(propertyId);
        entity.setPriceScore(tranrq.getPriceScore());
        entity.setEnvScore(tranrq.getEnvScore());
        entity.setServiceScore(tranrq.getServiceScore());
        entity.setContent(tranrq.getContent());
        entity.setCreateAt(LocalDateTime.now());

        try {
            reviewsRepo.save(entity);
            log.info("[REVIEW001] 新增評論成功，reviewId = {}", reviewId);
        } catch (Exception e) {
            log.error("[REVIEW001] 新增評論失敗，原因：{}", e.getMessage(), e);
            throw new InsertFailException("新增評論失敗");
        }

        REVIEW001Tranrs tranrs = new REVIEW001Tranrs();
        tranrs.setReviewId(reviewId);
        tranrs.setCreateAt(entity.getCreateAt());
        log.info("---- [REVIEW-001] 新增評論結束，reviewId = {} ----", reviewId);

        return new Res<REVIEW001Tranrs>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                tranrs
        );
    }
}
