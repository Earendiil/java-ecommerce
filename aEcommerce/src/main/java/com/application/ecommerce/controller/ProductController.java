package com.application.ecommerce.controller;

import java.io.IOException;

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
import org.springframework.web.multipart.MultipartFile;

import com.application.ecommerce.config.AppConstants;
import com.application.ecommerce.entity.Product;
import com.application.ecommerce.payload.ProductDTO;
import com.application.ecommerce.payload.ProductResponse;
import com.application.ecommerce.service.CategoryService;
import com.application.ecommerce.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	
	// We need ProductResponse for a List!
	// ProductDTO can only return 1 object!
	
	
	@GetMapping("/public/products")
	public ResponseEntity<ProductResponse> getAllProducts(
		@RequestParam(name = "keyword" , required = false) String keyword,
		@RequestParam(name = "category", required = false) String category,
		@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
		@RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false ) Integer pageSize,
		@RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
		@RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false)String sortOrder){
		ProductResponse productResponse = productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder, keyword, category);
		return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.OK);
	}
	
	@PostMapping("/admin/categories/{categoryId}/product")
	public ResponseEntity<ProductDTO> addProduct(@Valid @PathVariable Long categoryId,
												@RequestBody ProductDTO productDTO){
		ProductDTO savedproductDTO = productService.addProduct(categoryId, productDTO);
		return new ResponseEntity<ProductDTO>(savedproductDTO, HttpStatus.CREATED);
		
	}
	@GetMapping("/public/categories/{categoryId}/products")
	public ResponseEntity<ProductResponse> getProductByCategory(@PathVariable Long categoryId){
	ProductResponse productResponse = productService.searchByCategory(categoryId);
		return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.OK) ;
		
	}
	@GetMapping("public/products/keyword/{keyword}")
	public ResponseEntity<ProductResponse> getProductsByKeyword (@PathVariable String keyword) {
	ProductResponse productResponse = productService.searchByKeyword(keyword);
		return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.FOUND);
	}
	@PutMapping("/products/{productId}")
	public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long productId){
		ProductDTO updatedproductDTO = productService.updateProduct(productDTO,productId);
		return new ResponseEntity<ProductDTO>(updatedproductDTO,HttpStatus.OK);
		
	}
	
	@DeleteMapping("/admin/products/{productId}")
	public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
		ProductDTO productDTO = productService.deleteProductById(productId);
		return new ResponseEntity<ProductDTO>(productDTO, HttpStatus.OK) ;
		
	}
	
	@PutMapping("/products/{productId}/image")
	public ResponseEntity<ProductDTO> updateProductImage(
									@PathVariable Long productId,
									@RequestParam("image")MultipartFile image) throws IOException{
		
		ProductDTO updatedProduct= productService.updateProductImage(productId, image);
		return new ResponseEntity<ProductDTO>(updatedProduct, HttpStatus.OK);
	}
	
	
	
}
