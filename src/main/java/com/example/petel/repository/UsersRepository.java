package com.example.petel.repository;

import com.example.petel.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, String> {

    /** 用 accountId 查 user */
    @Query(value = "SELECT ID FROM PETEL_USERS WHERE ACCOUNT_ID = :accountId", nativeQuery = true)
    String findIdByAccountId(@Param("accountId") String accountId);
}
