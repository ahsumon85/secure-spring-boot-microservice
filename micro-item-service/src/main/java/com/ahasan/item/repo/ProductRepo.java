package com.ahasan.item.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ahasan.item.entity.ProductEntity;

public interface ProductRepo extends JpaRepository<ProductEntity, Long> {


	public ProductEntity findByProductId(Long empId);

}
