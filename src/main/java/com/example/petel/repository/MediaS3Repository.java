package com.example.petel.repository;

import com.example.petel.entity.MediaS3Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * S3 媒體檔案 Repository
 */
@Repository
public interface MediaS3Repository extends JpaRepository<MediaS3Entity, Long> {

    /**
     * 根據 Object Key 查詢
     * @param objectKey S3 Object Key
     * @return MediaS3Entity
     */
    Optional<MediaS3Entity> findByObjectKey(String objectKey);

    /**
     * 根據 Bucket 查詢
     * @param bucket S3 Bucket 名稱
     * @return S3 媒體列表
     */
    java.util.List<MediaS3Entity> findByBucket(String bucket);
}
