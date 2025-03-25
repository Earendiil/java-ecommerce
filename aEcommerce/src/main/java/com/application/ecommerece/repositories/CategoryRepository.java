package com.application.ecommerece.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.application.ecommerece.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

	Category findByCategoryName(String categoryName);

	Category findByCategoryId(Long categoryId);

	

	
}
