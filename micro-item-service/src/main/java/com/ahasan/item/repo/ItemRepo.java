package com.ahasan.item.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ahasan.item.entity.ItemEntity;

public interface ItemRepo extends JpaRepository<ItemEntity, Long> {

}
