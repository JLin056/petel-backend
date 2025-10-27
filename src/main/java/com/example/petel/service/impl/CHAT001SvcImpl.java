package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.ChatThreadEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InsertFailException;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.model.IdUtil;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.model.jwt.AccountPrincipal;
import com.example.petel.repository.*;
import com.example.petel.service.CHAT001Svc;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CHAT001SvcImpl implements CHAT001Svc {

    /** ChatThreadRepository */
    private final ChatThreadRepository threadRepo;
    /** OrdersRepository */
    private final OrdersRepository ordersRepo;
    /** UsersRepository */
    private final UsersRepository usersRepo;
    /** SellersRepository */
    private final SellersRepository sellersRepo;
    /** ObjectMapper */
    private final ObjectMapper objectMapper;

    /**
     * 建立聊天室
     * @param req
     * @param auth
     * @return
     * @throws DataNotFoundException
     * @throws InvalidInputException
     * @throws InsertFailException
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<CHAT001Tranrs> GetOrCreateByOrder(Req<CHAT001Tranrq> req,
                                                 AccountPrincipal auth)
            throws DataNotFoundException, InvalidInputException, InsertFailException {
        log.info("---- [CHAT-001] 建立聊天室 ----");
        String orderId = req.getTranrq().getOrderId();

        // 檢查訂單，並取得 userId, sellerId
        Map<String, Object> userAndSellerByOrderId = ordersRepo.findUserAndSellerByOrderId(orderId);
        if (userAndSellerByOrderId == null || userAndSellerByOrderId.isEmpty()) {
            log.warn("[CHAT001] 查無訂單，orderId = {}", orderId);
            throw new DataNotFoundException("查無該筆訂單");
        }
        String userId = (String) userAndSellerByOrderId.get("USERID");
        String sellerId = (String) userAndSellerByOrderId.get("SELLERID");

        // 訂單資訊驗證
        if (userId == null || sellerId == null) {
            log.warn("[CHAT-001] 訂單缺少 userId 或 sellerId, orderId={}", orderId);
            throw new DataNotFoundException("訂單資訊不完整");
        }

        // 取得 使用者 與 賣家 Account ID
        String userAccountId = usersRepo.findByAccountByUserId(userId);
        String sellerAccountId = sellersRepo.findByAccountBySellerId(sellerId);

        if (userAccountId == null) {
            log.warn("[CHAT001] 查無對應使用者帳號，userId={}", userId);
            throw new DataNotFoundException("找不到使用者帳號");
        }
        if (sellerAccountId == null) {
            log.warn("[CHAT001] 查無對應商家帳號，sellerId={}", sellerId);
            throw new DataNotFoundException("找不到商家帳號");
        }

          // 開發階段 先把驗證拿掉
//        String accountId = auth.getAccountId();
//        String role = auth.getRole();
//        boolean isUser = false;
//        boolean isSeller = false;
//
//        if ("user".equalsIgnoreCase(role)) {
//            isUser = accountId != null && accountId.equals(userAccountId);
//        } else if ("seller".equalsIgnoreCase(role)) {
//            isSeller = accountId != null && accountId.equals(sellerAccountId);
//        } else {
//            log.warn("[CHAT001] 未支援之角色，role={}", role);
//            throw new InvalidInputException("未支援之角色");
//        }
//
//        if (!isUser && !isSeller) {
//            log.warn("[CHAT001] 認證失敗，無權建立聊天室, orderId={}, accountId={}, role={}",
//                    orderId, accountId, role);
//            throw new InvalidInputException("認證失敗，無權建立聊天室");
//        }

        // 檢查是否已建立過聊天室
        Optional<ChatThreadEntity> threadByOrderId = threadRepo.findByOrderId(orderId);
        // 建立過聊天室
        if (threadByOrderId.isPresent()) {
            return toSuccessRes(threadByOrderId.get());
        }

        // 新增
        String maxId = threadRepo.findMaxId();
        String threadId = IdUtil.generateTableId("T", maxId);

        ChatThreadEntity chatThreadEntity = new ChatThreadEntity();
        chatThreadEntity.setId(threadId);
        chatThreadEntity.setOrderId(orderId);
        chatThreadEntity.setUserId(userAccountId);
        chatThreadEntity.setSellerId(sellerAccountId);
        chatThreadEntity.setStatus("open");
        chatThreadEntity.setLastReadMsgIdBuyer(null);
        chatThreadEntity.setLastReadMsgIdSeller(null);

        try {
            threadRepo.save(chatThreadEntity);
            log.info("[CHAT-001] 新增聊天室成功 threadId={}", threadId);
            return toSuccessRes(chatThreadEntity);
        } catch (Exception e) {
            log.error("[CHAT-001] 建立失敗，orderId={}", orderId);
            throw new InsertFailException("新增聊天室失敗");
        }
    }

    /**
     * res 格式
     * @param e
     * @return
     */
    private Res<CHAT001Tranrs> toSuccessRes(ChatThreadEntity e) {
        CHAT001Tranrs dto = objectMapper.convertValue(e, CHAT001Tranrs.class);
        dto.setTopic("/topic/chat." + e.getId());
        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), dto);
    }
}
