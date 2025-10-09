package com.example.petel.repository;

import com.example.petel.entity.PetelOrderItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetelRoomInventoriesRepository extends JpaRepository<PetelOrderItemsEntity, Long> {
}