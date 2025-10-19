package com.example.petel.repository;

import com.example.petel.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, String> {

    /**
     * 用 accountId 查 user
     * 
     * @param accountId
     * @return
     */
    @Query(value = "SELECT ID FROM PETEL_USERS WHERE ACCOUNT_ID = :accountId", nativeQuery = true)
    String findIdByAccountId(@Param("accountId") String accountId);

    /**
     * 查目前最大ID
     * 
     * @return
     */
    @Query("select max(e.id) from UsersEntity e")
    String findMaxId();

    /**
     * 檢查 accountID 是否存在
     * 
     * @param accountId
     * @return
     */
    boolean existsByAccountId(String accountId);

    /**
     * 用 userID 查 accountID
     * 
     * @param userId
     * @return
     */
    @Query(value = "SELECT ACCOUNT_ID FROM PETEL_USERS WHERE ID = :userId", nativeQuery = true)
    String findByAccountByUserId(@Param("userId") String userId);

    /**
     * 用 account ID 找資料
     * @param accountId
     * @return
     */
    Optional<UsersEntity> findByAccountId(String accountId);
}
