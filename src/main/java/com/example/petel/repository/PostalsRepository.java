package com.example.petel.repository;

import com.example.petel.entity.PostalsEntity;
import com.example.petel.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostalsRepository extends JpaRepository<PostalsEntity, String> {
    List<PostalsEntity> findAll();

    /**
     * 根據縣市和區域查詢郵遞區號ID
     * @param city 縣市
     * @param district 區域
     * @return 郵遞區號實體
     */
    Optional<PostalsEntity> findByCityAndDistrict(String city, String district);
}
