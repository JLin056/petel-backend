package com.example.petel.repository;

import com.example.petel.entity.AccountsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<AccountsEntity, Long> {

    boolean existsByEmailIgnoreCase(String email);
}
