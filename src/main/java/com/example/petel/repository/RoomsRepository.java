package com.example.petel.repository;

import com.example.petel.entity.RoomsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomsRepository extends JpaRepository<RoomsEntity, String> {

    /**
     * 查目前最大的表格 ID
     *
     * @return ID
     */
    @Query("select max(e.id) from RoomsEntity e")
    String findMaxId();

    /**
     * 查旅館ID
     *
     * @param propertyId
     * @return propertyId
     */
    List<RoomsEntity> findByPropertyId(String propertyId);
}