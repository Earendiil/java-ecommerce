package com.application.ecommerce.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.application.ecommerce.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product>{

	List<Product> findByProductNameLikeIgnoreCase(String keyword);

	
}
