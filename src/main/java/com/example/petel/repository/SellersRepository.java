package com.example.petel.repository;

import com.example.petel.entity.SellersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellersRepository extends JpaRepository<SellersEntity, Integer> {

}
