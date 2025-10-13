package com.example.petel.repository;

import com.example.petel.entity.MediaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 媒體檔案 Repository
 */
@Repository
public interface MediaRepository extends JpaRepository<MediaEntity, Long> {

    /**
     * 根據儲存類型查詢
     * @param storageType 儲存類型
     * @return 媒體列表
     */
    List<MediaEntity> findByStorageType(String storageType);

    /**
     * 根據可見性查詢
     * @param visibility 可見性
     * @return 媒體列表
     */
    List<MediaEntity> findByVisibility(String visibility);
}
