package com.example.petel.repository;

import com.example.petel.entity.LicenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LicenseRepository extends JpaRepository<LicenseEntity, String> {

    Optional<LicenseEntity> findByNameAndBusinessCode(String name, String businessCode);
}
