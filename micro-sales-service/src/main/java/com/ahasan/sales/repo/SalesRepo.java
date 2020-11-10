package com.ahasan.sales.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ahasan.sales.entity.SalesEntity;

public interface SalesRepo extends JpaRepository<SalesEntity, Long> {

	SalesEntity findBySaleId(Long userId);
}
