package com.application.ecommerece.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.application.ecommerece.entity.Category;
import com.application.ecommerece.exceptions.APIException;
import com.application.ecommerece.exceptions.ResourceNotFoundException;
import com.application.ecommerece.payload.CategoryDTO;
import com.application.ecommerece.payload.CategoryResponse;
import com.application.ecommerece.repositories.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize,String sortBy,String sortOrder) {
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
				? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		
		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		Page<Category> categoryPage = categoryRepository.findAll(pageDetails);
		List<Category> allCategories = categoryPage.getContent();
		if (allCategories.isEmpty()) {
			throw new APIException("No categories exist!");
		}
			
		List<CategoryDTO> categoryDTOS = allCategories.stream()
				.map(category -> modelMapper.map(category, CategoryDTO.class))
				.toList();
		CategoryResponse categoryResponse = new CategoryResponse();
		categoryResponse.setContent(categoryDTOS);
		categoryResponse.setPageNumber(categoryPage.getNumber());
		categoryResponse.setPageSize(categoryPage.getSize());
		categoryResponse.setTotalElements(categoryPage.getTotalElements());
		categoryResponse.setTotalPages(categoryPage.getTotalPages());
		categoryResponse.setLastPage(categoryPage.isLast());
 		
		
		return categoryResponse;
	}

	@Override
	public CategoryDTO createCategory(CategoryDTO categoryDTO) {
		Category category = modelMapper.map(categoryDTO, Category.class);
		Category existingCategory  = categoryRepository.findByCategoryName(category.getCategoryName());
		if (existingCategory  != null) {
			throw new APIException("Category with the name " + category.getCategoryName()+ " already exists!");
		}
		Category savedCategory = categoryRepository.save(category);
		return modelMapper.map(savedCategory, CategoryDTO.class) ;
	}

	@Override
	public CategoryDTO deleteCategory(Long categoryId) {
		Category category = categoryRepository.findById(categoryId).
				orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));
		categoryRepository.delete(category);
		return modelMapper.map(category, CategoryDTO.class);
	}

	@Override
	public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
		
		Category existingCategory = categoryRepository.findById(categoryId)
				.orElseThrow(()-> new ResourceNotFoundException("Category", "CategoryId", categoryId));
		existingCategory.setCategoryName(categoryDTO.getCategoryName());
	   
		
		Category updatedCategory = categoryRepository.save(existingCategory);
		return modelMapper.map(updatedCategory, CategoryDTO.class);
			
	}

	@Override
	public CategoryDTO findCategory(Long categoryId) {
		Category category =	categoryRepository.findByCategoryId(categoryId);
		return modelMapper.map(category, CategoryDTO.class);
	}

	

	
	
	
	
	
	
	
}
