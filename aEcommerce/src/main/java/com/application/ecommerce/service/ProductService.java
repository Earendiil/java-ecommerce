package com.application.ecommerce.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.application.ecommerce.payload.ProductDTO;
import com.application.ecommerce.payload.ProductResponse;

public interface ProductService {

	ProductDTO addProduct(Long categoryId, ProductDTO productDTO);

	ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String keyword, String category);

	ProductResponse searchByCategory(Long categoryId);

	ProductResponse searchByKeyword(String keyword);

	ProductDTO updateProduct(ProductDTO productDTO, Long productId);

	ProductDTO deleteProductById(Long productId);

	ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;

	
	
}
