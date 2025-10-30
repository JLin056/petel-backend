package com.example.petel.repository;

import com.example.petel.entity.PostalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostalRepository extends JpaRepository<PostalEntity, String> {
    /**
     * 根據縣市和區域查詢郵遞區號ID
     * @param city 縣市
     * @param district 區域
     * @return 郵遞區號實體
     */
    Optional<PostalEntity> findByCityAndDistrict(String city, String district);
}
