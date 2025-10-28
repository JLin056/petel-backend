package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.model.jwt.AccountPrincipal;
import com.example.petel.model.sql.SqlAction;
import com.example.petel.model.sql.SqlUtils;
import com.example.petel.service.CHAT002Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CHAT002SvcImpl implements CHAT002Svc {

    /** SqlAction */
    private final SqlAction sqlAction;
    /** SqlUtils */
    private final SqlUtils sqlUtils;
    /** ZoneId TPE */
    private static final ZoneId TPE = ZoneId.of("Asia/Taipei");

    @Override
    public Res<CHAT002Tranrs> getThreads(AccountPrincipal auth, Req<CHAT002Tranrq> req) throws IOException {
        log.info("---- [CHAT-002] 取得聊天室列表 ----");
        boolean isBuyer = "user".equalsIgnoreCase(auth.getRole());
        String accountId = auth.getAccountId();

        // 計算頁數
        CHAT002Tranrq tranrq = req.getTranrq();
        Integer pageNumber = tranrq.getPageNumber();
        Integer pageSize = tranrq.getPageSize();
        int offset = (pageNumber - 1) * pageSize;

        log.info("[CHAT-002] role={}, accountId={}, pageNumber={}, pageSize={}",
                auth.getRole(), accountId, pageNumber, pageSize);

        // 總筆數
        String countSqlFile = isBuyer ? "CHAT002_COUNT_BUYER.sql" : "CHAT002_COUNT_SELLER.sql";
        HashMap<String, Object> countMap = new HashMap<>();
        countMap.put("accountId", accountId);

        log.debug("[CHAT-002] 執行總筆數查詢 - sqlFile={}", countSqlFile);
        String countSql = sqlUtils.getDynamicQuerySQL(countSqlFile, countMap);
        List<Map<String, Object>> cntRows = sqlAction.queryForList(countSql, countMap);
        int totalCount = 0;
        if (!cntRows.isEmpty()) {
            Object cntObj = cntRows.get(0).get("CNT");
            if (cntObj instanceof Number n) {
                totalCount = n.intValue();
            }
        }
        log.info("[CHAT-002] 查詢總筆數完成 - totalCount={}", totalCount);

        // 顯示名稱＋OrderId
        String querySqlFile = isBuyer ? "CHAT002_QUERY_BUYER.sql" : "CHAT002_QUERY_SELLER.sql";
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("accountId", accountId);
        queryMap.put("offset", offset);
        queryMap.put("limit", pageSize);

        log.debug("[CHAT-002] 執行聊天室列表查詢 - sqlFile={}", querySqlFile);
        String querySql = sqlUtils.getDynamicQuerySQL(querySqlFile, queryMap);
        List<Map<String, Object>> nameList = sqlAction.queryForList(querySql, queryMap);
        // 如果查無資料
        if (nameList == null || nameList.isEmpty()) {
            log.info("[CHAT-002] 查無聊天室資料，回傳空列表");
            CHAT002Tranrs emptyTranrs = new CHAT002Tranrs(pageSize, pageNumber, 0, 0, Collections.emptyList());
            return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), emptyTranrs);
        }

        log.info("[CHAT-002] 查詢到 {} 筆聊天室基本資料", nameList.size());

        // 取得 所有 threadId
        ArrayList<Object> threadIds = new ArrayList<>(nameList.size());
        for (Map<String, Object> row: nameList) {
            Object threadIdObj = row.get("THREAD_ID");
            if (threadIdObj == null) {
                log.warn("[CHAT-002] 發現 null THREAD_ID，跳過此筆資料");
                continue;
            }
            threadIds.add(threadIdObj.toString());
        }
        log.debug("[CHAT-002] 收集到 {} 個有效的 threadId", threadIds.size());

        // 取 最後一則訊息
        HashMap<String, Object> lastParam = new HashMap<>();
        lastParam.put("ids", threadIds);

        log.debug("[CHAT-002] 執行最後訊息查詢 - threadIds.size={}", threadIds.size());
        String lastSql = sqlUtils.getDynamicQuerySQL("CHAT002_QUERY_MESSAGE.sql", lastParam);
        List<Map<String, Object>> lastList = sqlAction.queryForList(lastSql, lastParam);

        log.debug("[CHAT-002] 查詢到 {} 筆最後訊息", lastList != null ? lastList.size() : 0);

        HashMap<String, Map<String, Object>> lastMap = new HashMap<>();
        for (Map<String, Object> row: lastList) {
            String threadId = row.get("THREAD_ID").toString();
            if (threadId != null) lastMap.put(threadId, row);
        }


        // Res
        List<CHAT002TranrsChats> chats = new ArrayList<>(nameList.size());
        for (Map<String, Object> row: nameList) {
            Object threadIdObj = row.get("THREAD_ID");
            Object orderIdObj = row.get("ORDER_ID");
            Object displayNameObj = row.get("DISPLAY_NAME");

            if (threadIdObj == null || orderIdObj == null || displayNameObj == null) {
                log.warn("[CHAT-002] 資料不完整，跳過此筆: threadId={}, orderId={}, displayName={}",
                        threadIdObj, orderIdObj, displayNameObj);
                continue;
            }

            String threadId = threadIdObj.toString();
            String orderId = orderIdObj.toString();
            String displayName = displayNameObj.toString();

            String lastMessage = null;
            LocalDateTime lastMessageTime = null;

            Map<String, Object> lm = lastMap.get(threadId);
            if (lm != null) {
                Object contentObj = lm.get("LAST_MSG_CONTENT");
                if (contentObj != null) {
                    lastMessage = contentObj.toString();
                }

                Object tsObj = lm.get("LAST_MSG_TIME");
                if (tsObj instanceof Timestamp ts) {
                    lastMessageTime = ts.toLocalDateTime();
                }
            }

            chats.add(new CHAT002TranrsChats(
                    threadId,
                    orderId,
                    displayName,
                    lastMessage,
                    lastMessageTime
            ));
        }

        int totalPage = (int) Math.ceil((double) totalCount / pageSize);
        CHAT002Tranrs body = new CHAT002Tranrs(pageSize, pageNumber, totalPage, totalCount, chats);
        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), body);
    }
}
