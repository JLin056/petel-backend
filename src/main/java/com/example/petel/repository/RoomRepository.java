package com.example.petel.repository;

import com.example.petel.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

    /**
     * 查詢該房型在某日的庫存狀態
     *
     * @param roomId Long
     * @return 對應到該房型編號和入住日期的庫存資料
     */
    Optional<RoomEntity> findByRoomId(Long roomId);
}