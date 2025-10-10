package com.example.petel.repository;

import com.example.petel.entity.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PropertyRepository extends JpaRepository<PropertyEntity, Long> {
}
