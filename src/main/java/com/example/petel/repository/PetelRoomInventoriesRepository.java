package com.example.petel.repository;

import com.example.petel.entity.PetelRoomInventoriesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetelRoomInventoriesRepository extends JpaRepository<PetelRoomInventoriesEntity, Long> {
    List<PetelRoomInventoriesEntity> findByProductIdAndStayDate(Long productId, String stayDate);
}