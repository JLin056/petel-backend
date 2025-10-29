package com.example.petel.repository;

import com.example.petel.entity.NotificationEventsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationEventsRepository extends JpaRepository<NotificationEventsEntity, String> {

    /**
     * 查詢特定帳號在指定時間之後的事件，依發送時間升序排序
     * （用於斷線重連後補發事件）
     * @param accountId 帳號 ID
     * @param after 起始時間
     * @return 事件列表
     */
    List<NotificationEventsEntity> findByAccountIdAndSentAtAfterOrderBySentAtAsc(
            String accountId, OffsetDateTime after);

    /**
     * 根據帳號 ID 和事件 ID 查詢第一筆事件
     * （用於檢查事件是否已存在，避免重複發送）
     * @param accountId 帳號 ID
     * @param eventId 事件 ID
     * @return 事件
     */
    Optional<NotificationEventsEntity> findFirstByAccountIdAndEventId(String accountId, String eventId);

    /**
     * 查詢目前最大的表格 ID
     * @return ID
     */
    @Query("select max(e.id) from NotificationEventsEntity e")
    String findMaxId();
}
