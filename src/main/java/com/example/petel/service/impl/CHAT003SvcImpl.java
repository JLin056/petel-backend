package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.ChatThreadEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.model.jwt.AccountPrincipal;
import com.example.petel.model.sql.SqlAction;
import com.example.petel.model.sql.SqlUtils;
import com.example.petel.repository.*;
import com.example.petel.service.CHAT003Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CHAT003SvcImpl implements CHAT003Svc {

    /** ChatThreadRepository */
    private final ChatThreadRepository chatThreadRepo;
    /** OrdersRepository */
    private final OrdersRepository ordersRepo;
    /** UsersRepository */
    private final UsersRepository usersRepo;
    /** SellersRepository */
    private final SellersRepository sellersRepo;
    /** SqlAction */
    private final SqlAction sqlAction;
    /** SqlUtils */
    private final SqlUtils sqlUtils;

    public Res<CHAT003Tranrs> getRoom(AccountPrincipal auth, Req<CHAT003Tranrq> req)
            throws DataNotFoundException, IOException {
        log.info("---- [CHAT-003] 取得單筆聊天室內容 ----");
        boolean isBuyer = "user".equalsIgnoreCase(auth.getRole());
        String accountId = auth.getAccountId();

        CHAT003Tranrq tranrq = req.getTranrq();
        Integer pageNumber = tranrq.getPageNumber();
        Integer pageSize = tranrq.getPageSize();
        int offset = (pageNumber - 1) * pageSize;

        log.info("[CHAT-003] role={}, accountId={}, pageNumber={}, pageSize={}",
                auth.getRole(), accountId, pageNumber, pageSize);

        // 查 聊天室資料
        String threadId = tranrq.getThreadId();
        log.debug("[CHAT-003] 查詢聊天室資料，threadId={}", threadId);
        ChatThreadEntity chatThreadEntity = chatThreadRepo.findById(threadId)
                .orElseThrow(() -> new DataNotFoundException("查無此聊天室"));
        log.debug("[CHAT-003] 查到聊天室: buyer={}, seller={}, orderId={}",
                chatThreadEntity.getUserId(), chatThreadEntity.getSellerId(), chatThreadEntity.getOrderId());

        String orderStatus = ordersRepo.findStatusById(chatThreadEntity.getOrderId());
        log.debug("[CHAT-003] 訂單狀態: {}", orderStatus);
        String name;
        if (isBuyer) {
            name = sellersRepo.findNameByAccountId(chatThreadEntity.getSellerId());
            log.debug("[CHAT-003] 登入者為用戶，對方商家名稱={}", name);
        } else {
            name = usersRepo.findNameByAccountId(chatThreadEntity.getUserId());
            log.debug("[CHAT-003] 登入者為商家，對方用戶名稱={}", name);
        }

        CHAT003TranrsRoom tranrsRoom = new CHAT003TranrsRoom();
        tranrsRoom.setThreadId(threadId);
        tranrsRoom.setRole(auth.getRole());
        tranrsRoom.setBuyerAccountId(chatThreadEntity.getUserId());
        tranrsRoom.setSellerAccountId(chatThreadEntity.getSellerId());
        tranrsRoom.setDisplayName(name);
        tranrsRoom.setOrderId(chatThreadEntity.getOrderId());
        tranrsRoom.setOrderStatus(orderStatus);

        HashMap<String, Object> param = new HashMap<>();
        param.put("threadId", threadId);
        param.put("offset", offset);
        param.put("limit", pageSize);

        // 查訊息列表
        String querySQL = sqlUtils.getDynamicQuerySQL("CHAT003_QUERY.sql", param);
        List<Map<String, Object>> rows = sqlAction.queryForList(querySQL, param);
        List<CHAT003TranrsMessages> messagesList = new ArrayList<>();
        for (Map<String, Object> row: rows) {
            CHAT003TranrsMessages messages = new CHAT003TranrsMessages();

            // messageId
            Object msgIdObj = row.get("MESSAGE_ID");
            if (msgIdObj != null) {
                messages.setMessageId(msgIdObj.toString());
            }

            // senderId
            Object senderIdObj = row.get("SENDER_ID");
            if (senderIdObj != null) {
                messages.setSenderId(senderIdObj.toString());
            }

            // type
            Object typeObj = row.get("TYPE");
            if (typeObj != null) {
                messages.setType(typeObj.toString());
            }

            // content
            Object contentObj = row.get("CONTENT");
            if (contentObj != null) {
                messages.setContent(contentObj.toString());
            }

            Object createdAtObj = row.get("CREATED_AT");
            if (createdAtObj instanceof Timestamp ts) {
                messages.setCreatedAt(ts.toLocalDateTime());
            }

            messagesList.add(messages);
         }

        log.info("[CHAT-003] 訊息查詢完成，共取得 {} 筆", messagesList.size());
        CHAT003Tranrs chat003Tranrs = new CHAT003Tranrs(tranrsRoom, messagesList);

        return new Res<CHAT003Tranrs>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                chat003Tranrs
        );
    }
}
