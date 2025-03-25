package com.application.ecommerece.service;

import com.application.ecommerece.payload.CategoryDTO;
import com.application.ecommerece.payload.CategoryResponse;

public interface CategoryService {
	
	CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
	
	CategoryDTO createCategory(CategoryDTO categoryDTO);

	CategoryDTO deleteCategory(Long categoryId);

	CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);

	CategoryDTO findCategory(Long categoryId);
	

}
