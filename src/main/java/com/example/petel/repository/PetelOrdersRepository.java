package com.example.petel.repository;

import com.example.petel.entity.PetelOrdersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetelOrdersRepository extends JpaRepository<PetelOrdersEntity, Long> {
}