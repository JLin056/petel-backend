package com.example.petel.repository;

import com.example.petel.entity.NotificationsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationsRepository extends JpaRepository<NotificationsEntity, String> {

    /**
     * 根據帳號 ID 查詢通知列表，依建立時間降序排序
     * @param accountId 帳號 ID
     * @return 通知列表
     */
    List<NotificationsEntity> findByAccountIdOrderByCreatedAtDesc(String accountId);

    /**
     * 統計特定帳號的特定狀態通知數量
     * @param accountId 帳號 ID
     * @param status 通知狀態 (UNREAD/READ)
     * @return 通知數量
     */
    long countByAccountIdAndStatus(String accountId, String status);

    /**
     * 查目前最大的表格 ID
     * @return ID
     */
    @Query("select max(e.id) from NotificationsEntity e")
    String findMaxId();
}
