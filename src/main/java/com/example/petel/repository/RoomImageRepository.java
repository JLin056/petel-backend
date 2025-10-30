package com.example.petel.repository;

import com.example.petel.entity.RoomImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 房型圖片關聯 Repository
 */
@Repository
public interface RoomImageRepository extends JpaRepository<RoomImageEntity, RoomImageEntity.RoomImageId> {

    /**
     * 根據房型ID查詢所有圖片
     * @param roomId 房型ID
     * @return 圖片列表
     */
    List<RoomImageEntity> findByRoomId(String roomId);

    /**
     * 根據媒體ID查詢
     * @param mediaId 媒體ID
     * @return 圖片列表
     */
    List<RoomImageEntity> findByMediaId(String mediaId);

    /**
     * 根據房型ID查詢最大排序順序
     * @param roomId 房型ID
     * @return 最大排序順序
     */
    @Query("SELECT COALESCE(MAX(r.sortOrder), 0) FROM RoomImageEntity r WHERE r.roomId = :roomId")
    Integer findMaxSortOrderByRoomId(String roomId);

    void deleteByRoomId(String roomId);
}
