package com.example.petel.repository;

import com.example.petel.entity.RoomsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomsRepository extends JpaRepository<RoomsEntity, String> {
}