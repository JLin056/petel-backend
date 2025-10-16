package com.example.petel.repository;

import com.example.petel.entity.AccountsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
    @Query(value = """
        SELECT NVL2(
                 MAX(TO_NUMBER(SUBSTR(a.ID, 2))),
                 'A' || LPAD(MAX(TO_NUMBER(SUBSTR(a.ID, 2))), 9, '0'),
                 NULL
               )
        FROM PETEL_ACCOUNTS a
        """, nativeQuery = true)
    String findMaxAccountId();
}
