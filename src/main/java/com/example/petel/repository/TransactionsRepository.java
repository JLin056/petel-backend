package com.example.petel.repository;

import com.example.petel.entity.TransactionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionsRepository extends JpaRepository<TransactionsEntity, String> {
    Optional<TransactionsEntity> findByOrderId(String orderId);
}