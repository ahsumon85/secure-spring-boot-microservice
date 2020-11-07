package com.sales.rest.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sales.rest.entity.SalesEntity;

public interface SalesRepo extends JpaRepository<SalesEntity, Long> {

	SalesEntity findBySaleId(Long userId);
}
