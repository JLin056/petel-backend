package com.example.petel.repository;

import com.example.petel.entity.RoomInventoriesEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomInventoriesRepository extends JpaRepository<RoomInventoriesEntity, String> {

    /**
     * 查詢該房型在某日的庫存狀態
     *
     * @param roomId   String
     * @param stayDate String
     * @return 對應到該房型編號和入住日期的庫存資料
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE) // 悲觀鎖定：防止超賣
    Optional<RoomInventoriesEntity> findByRoomIdAndStayDate(String roomId, String stayDate);
}