package com.application.ecommerece.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.application.ecommerece.payload.ProductDTO;
import com.application.ecommerece.payload.ProductResponse;

public interface ProductService {

	ProductDTO addProduct(Long categoryId, ProductDTO productDTO);

	ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

	ProductResponse searchByCategory(Long categoryId);

	ProductResponse searchByKeyword(String keyword);

	ProductDTO updateProduct(ProductDTO productDTO, Long productId);

	ProductDTO deleteProductById(Long productId);

	ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;

	
	
}
