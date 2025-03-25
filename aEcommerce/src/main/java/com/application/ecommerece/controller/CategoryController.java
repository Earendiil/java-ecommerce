package com.application.ecommerece.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.application.ecommerece.config.AppConstants;
import com.application.ecommerece.payload.CategoryDTO;
import com.application.ecommerece.payload.CategoryResponse;
import com.application.ecommerece.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CategoryController {

	
	@Autowired
	private CategoryService categoryService;
	
	
	@GetMapping("/public/categories")
	public ResponseEntity<CategoryResponse> getAllCategories(
			@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
			@RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
			@RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_CATEGORIES_BY) String sortBy,
			@RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_DIR) String sortOrder
			){
		CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber,pageSize, sortBy,sortOrder);
		return ResponseEntity.ok(categoryResponse);
	}
	
	@PostMapping("/public/categories/create")
	public ResponseEntity<CategoryDTO> createCategory (@Valid @RequestBody CategoryDTO categoryDTO) {
		CategoryDTO savedCategory = categoryService.createCategory(categoryDTO);
		return  new ResponseEntity<CategoryDTO>(savedCategory,HttpStatus.CREATED);
		
	}
	@DeleteMapping("/admin/categories/{categoryId}")
	public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId) {
			CategoryDTO deleted = categoryService.deleteCategory(categoryId);
			return  ResponseEntity.ok(deleted);
		
	}
	
	@PutMapping("/public/categories/{categoryId}")
	public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long categoryId,
												@RequestBody CategoryDTO categoryDTO){
	CategoryDTO savedCategory =	categoryService.updateCategory(categoryDTO, categoryId);
			return new ResponseEntity<CategoryDTO>(savedCategory, HttpStatus.OK);
	}
	@GetMapping("/public/categories/{categoryId}")
	public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long categoryId){
		CategoryDTO categoryDTO = categoryService.findCategory(categoryId);
		return new ResponseEntity<CategoryDTO>(categoryDTO, HttpStatus.OK);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
