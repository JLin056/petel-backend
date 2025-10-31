package com.example.petel.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * SSE 通知中心 - 管理所有 SSE 連線
 * 使用 ConcurrentHashMap + CopyOnWriteArrayList 確保多執行緒安全
 */
@Slf4j
@Component
public class NotificationHub {

    /**
     * 連線池：accountId -> List of SseEmitter
     * ConcurrentHashMap: 多執行緒安全的 Map，適合高併發讀寫
     * CopyOnWriteArrayList: 寫時複製陣列，適合讀多寫少的場景（SSE 連線建立/斷開較少，廣播訊息較多）
     */
    private final ConcurrentMap<String, CopyOnWriteArrayList<SseEmitter>> pool = new ConcurrentHashMap<>();

    /**
     * 註冊新的 SSE 連線
     * @param accountId 帳號 ID
     * @return SseEmitter 實例
     */
    public SseEmitter register(String accountId) {
        // 建立新連線，設定 30 分鐘超時
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L);

        // 將 emitter 加入到對應帳號的連線列表
        pool.computeIfAbsent(accountId, k -> new CopyOnWriteArrayList<>()).add(emitter);

        // 註冊連線完成時的清理邏輯（正常斷線）
        emitter.onCompletion(() -> {
            pool.getOrDefault(accountId, new CopyOnWriteArrayList<>()).remove(emitter);
            log.info("[NotificationHub] 連線已關閉，帳號：{}，剩餘連線數：{}", accountId, getConnectionCount(accountId));
        });

        // 註冊連線逾時時的清理邏輯（連線逾時）
        emitter.onTimeout(() -> {
            pool.getOrDefault(accountId, new CopyOnWriteArrayList<>()).remove(emitter);
            log.warn("[NotificationHub] 連線超時，帳號：{}，剩餘連線數：{}", accountId, getConnectionCount(accountId));
        });

        // 註冊錯誤處理
        emitter.onError((throwable) -> {
            pool.getOrDefault(accountId, new CopyOnWriteArrayList<>()).remove(emitter);
            log.error("[NotificationHub] 連線錯誤，帳號：{}，剩餘連線數：{}", accountId, getConnectionCount(accountId));
        });

        log.info("[NotificationHub] 新連線已註冊，帳號：{}，當前連線數：{}", accountId, getConnectionCount(accountId));

        return emitter;
    }

    /**
     * 發送訊息給特定帳號的所有連線
     * @param accountId 帳號 ID
     * @param eventName 事件名稱（例如：notification, order-status）
     * @param eventId 事件 ID（用於客戶端追蹤）
     * @param payload 訊息內容
     */
    public void sendTo(String accountId, String eventName, String eventId, Object payload) {
        var list = pool.getOrDefault(accountId, new CopyOnWriteArrayList<>());

        if (list.isEmpty()) {
            log.warn("[NotificationHub] 帳號 {} 沒有活躍的 SSE 連線", accountId);
            return;
        }

        log.info("[NotificationHub] 準備推播給帳號 {}，事件：{}，連線數：{}", accountId, eventName, list.size());

        int successCount = 0;
        int failCount = 0;

        for (SseEmitter e : list) {
            try {
                e.send(SseEmitter.event().name(eventName).id(eventId).data(payload));
                successCount++;
                log.debug("[NotificationHub] 推播成功到帳號 {} 的一個連線", accountId);
            } catch (Exception ex) {
                failCount++;
                log.error("[NotificationHub] 推播失敗到帳號 {} 的一個連線，錯誤：{}", accountId, ex.getMessage());
                // 發送失敗時關閉連線（會觸發 onError/onCompletion 自動清理）
                try {
                    e.completeWithError(ex);
                } catch (Exception ignored) {
                    // 可能已經關閉，忽略錯誤
                }
            }
        }

        log.info("[NotificationHub] 推播完成，帳號：{}，成功：{}，失敗：{}", accountId, successCount, failCount);
    }

    /**
     * 發送心跳訊息給特定帳號的所有連線
     * 用於保持連線活躍，避免被代理伺服器或防火牆斷開
     * @param accountId 帳號 ID
     */
    public void heartbeat(String accountId) {
        pool.getOrDefault(accountId, new CopyOnWriteArrayList<>()).forEach(e -> {
            try {
                e.send(SseEmitter.event().comment("ping"));
            } catch (Exception ex) {
                // 發送失敗時關閉連線
                e.complete();
            }
        });
    }

    /**
     * 取得特定帳號的連線數量
     * @param accountId 帳號 ID
     * @return 連線數量
     */
    public int getConnectionCount(String accountId) {
        return pool.getOrDefault(accountId, new CopyOnWriteArrayList<>()).size();
    }

    /**
     * 移除特定帳號的所有連線
     * @param accountId 帳號 ID
     */
    public void removeAllConnections(String accountId) {
        var list = pool.remove(accountId);
        if (list != null) {
            list.forEach(e -> {
                try {
                    e.complete(); // 結束連線
                } catch (Exception ignore) {
                    // emitter 可能早就結束，無妨
                }
            });
        }
    }
}
