package com.product.rest.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.product.rest.entity.ProductEntity;

public interface ProductRepo extends JpaRepository<ProductEntity, Long> {


	public ProductEntity findByProductId(Long empId);

}
