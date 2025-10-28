package com.example.petel.repository;

import com.example.petel.entity.SellersEntity;
import com.example.petel.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SellersRepository extends JpaRepository<SellersEntity, String> {
    /**
     * 查目前最大的表格 ID
     *
     * @return ID
     */
    @Query("select max(e.id) from SellersEntity e")
    String findMaxId();

    /** 用 accountId 查 seller */
    @Query(value = "SELECT ID FROM PETEL_SELLERS WHERE ACCOUNT_ID = :accountId", nativeQuery = true)
    String findIdByAccountId(@Param("accountId") String accountId);

    /**
     * 用 SellerId 查 AccountId
     * @param sellerId
     * @return
     */
    @Query(value = "SELECT ACCOUNT_ID FROM PETEL_SELLERS WHERE ID = :sellerId", nativeQuery = true)
    String findByAccountBySellerId(@Param("sellerId") String sellerId);

    /**
     * 用 accountId 查整個 SellersEntity
     * 由於 accountId 在 SellersEntity 中是唯一的，可以直接返回 Optional<SellersEntity>
     *
     * @param accountId 帳號 ID
     * @return 包含 SellersEntity 的 Optional
     */
    Optional<SellersEntity> findByAccountId(String accountId);

    /**
     * 檢查 accountID 是否存在
     *
     * @param accountId
     * @return
     */
    boolean existsByAccountId(String accountId);

    /**
     * 用 account ID 找名字
     * @param accountId
     * @return
     */
    @Query("SELECT e.name FROM SellersEntity e WHERE e.accountId = :accountId")
    String findNameByAccountId(@Param("accountId") String accountId);
}
