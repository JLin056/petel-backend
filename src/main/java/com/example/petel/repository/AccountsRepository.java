package com.example.petel.repository;

import com.example.petel.entity.AccountsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<AccountsEntity, String> {

    /**
     * 檢查 Email 是否存在
     * @param email Email
     * @return boolean
     */
    boolean existsByEmailIgnoreCase(String email);

    /**
     * 查該筆 Email 資料
     * @param email Email
     * @return 該筆資料
     */
    Optional<AccountsEntity> findByEmailIgnoreCase(String email);

    /**
     * 查目前最大
     * @return
     */
    @Query("select max(a.id) from AccountsEntity a")
    String findMaxAccountId();

    /**
     * 查 token version
     * @param id
     * @return
     */
    @Query("select a.tokenVersion from AccountsEntity a where a.id = :id")
    Integer findTokenVersionById(@Param("id") String id);

    /**
     * 用 id 查 Email
     * @param id
     * @return email
     */
    @Query("select a.email from AccountsEntity a where a.id = :id")
    String findEmailById(@Param("id") String id);

    /**
     * 用 Email 查 id
     * @param email
     * @return
     */
    @Query("SELECT a.id FROM AccountsEntity a WHERE LOWER(a.email) = LOWER(:email)")
    String findIdByEmailIgnoreCase(@Param("email") String email);
}
