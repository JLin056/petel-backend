package com.example.petel.repository;

import com.example.petel.entity.MediaBase64Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 媒體 Base64 資料 Repository
 */
@Repository
public interface MediaBase64Repository extends JpaRepository<MediaBase64Entity, String> {

    /**
     * 根據 Bucket 查詢
     * @param bucket Bucket 名稱
     * @return 媒體列表
     */
    List<MediaBase64Entity> findByBucket(String bucket);
}
