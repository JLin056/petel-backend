package com.example.petel.repository;

import com.example.petel.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, String> {
}
