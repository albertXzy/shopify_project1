package com.xzy.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xzy.springboot.model.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
	
}
