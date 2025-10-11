package com.example.petel.repository;

import com.example.petel.entity.VirtualAccountsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VirtualAccountsRepository extends JpaRepository<VirtualAccountsEntity, Long> {
}