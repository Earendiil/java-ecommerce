package com.application.ecommerece.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.application.ecommerece.entity.Cart;
import com.application.ecommerece.entity.Category;
import com.application.ecommerece.entity.Product;
import com.application.ecommerece.exceptions.APIException;
import com.application.ecommerece.exceptions.ResourceNotFoundException;
import com.application.ecommerece.payload.CartDTO;
import com.application.ecommerece.payload.ProductDTO;
import com.application.ecommerece.payload.ProductResponse;
import com.application.ecommerece.repositories.CartRepository;
import com.application.ecommerece.repositories.CategoryRepository;
import com.application.ecommerece.repositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private CartService cartService;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private FileService fileService;
	//Now it will take the image from the properties and we dont have to hardcode it
	@Value("${project.image}")
	private String path;
	

	@Override
	public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
		
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("category", "categoryId", categoryId));
	
	//	boolean productExists = false;
		
		List<Product> products = category.getProducts();
		for ( Product product : products) {
			if (product.getProductName().equals(productDTO.getProductName())) {
				throw new APIException("Product name already exists!");
			}
		}
		
		Product product = modelMapper.map(productDTO, Product.class);
		product.setImage("default.png");
		product.setCategory(category);
		double specialPrice = product.getPrice() -
				((product.getDiscount() * 0.01) * product.getPrice());
		product.setSpecialPrice(specialPrice);
		Product savedProduct = productRepository.save(product);
		return modelMapper.map(savedProduct, ProductDTO.class);
		
	}


	@Override
	public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
				? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		
		PageRequest pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		Page<Product> pageProducts = productRepository.findAll(pageDetails);
		
		List<Product> products = pageProducts.getContent();
		
		if (products.isEmpty())
			throw new APIException("No products exist!");
		
		List<ProductDTO> productDTOS = products.stream()
				.map(product -> modelMapper.map(product, ProductDTO.class))
				.toList();
		ProductResponse productResponse = new ProductResponse();
		productResponse.setContent(productDTOS);
		productResponse.setPageNumber(pageProducts.getNumber());
		productResponse.setPageSize(pageProducts.getSize());
		productResponse.setTotalElements(pageProducts.getTotalElements());
		productResponse.setTotalPages(pageProducts.getTotalPages());
		productResponse.setLastPage(pageProducts.isLast());
		return productResponse;
		
	}
	@Override
	public ProductResponse searchByCategory(Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("category", "categoryId", categoryId));
		
	//	List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);
		
		List<Product> products = category.getProducts();
		if (products.isEmpty()) 
			throw new ResourceNotFoundException("Category", "category name", categoryId);
		List<ProductDTO> productDTOS = products.stream()
				.map(product -> modelMapper.map(product, ProductDTO.class))
				.toList();
		ProductResponse productResponse = new ProductResponse();
		productResponse.setContent(productDTOS);
		return productResponse;
		
		
	}


	@Override
	public ProductResponse searchByKeyword(String keyword) {
		
		List<Product> products = productRepository.findByProductNameLikeIgnoreCase(keyword);
		if (products.isEmpty()) 
			throw new ResourceNotFoundException("Product", "product name", keyword);
		
		List<ProductDTO> productDTOS = products.stream()
				.map(product -> modelMapper.map(product, ProductDTO.class))
				.toList();
		ProductResponse productResponse = new  ProductResponse();
		productResponse.setContent(productDTOS);
		
		return productResponse;
	}


	@Override
	public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
		Product updatedProduct = productRepository.findById(productId)
				.orElseThrow (() -> new ResourceNotFoundException("Product", "product name", productId));
		//With this line we can replace the requested parameters to productDTO
		Product product = modelMapper.map(productDTO, Product.class);
		
		updatedProduct.setDescription(product.getDescription());
		updatedProduct.setDiscount(product.getDiscount());
		updatedProduct.setImage(product.getImage());
		updatedProduct.setPrice(product.getPrice());
		updatedProduct.setProductName(product.getProductName());
		updatedProduct.setQuantity(product.getQuantity());
		updatedProduct.setSpecialPrice(product.getSpecialPrice());
		productRepository.save(updatedProduct);
		
		  List<Cart> carts = cartRepository.findCartsByProductId(productId);

	        List<CartDTO> cartDTOs = carts.stream().map(cart -> {
	            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

	            List<ProductDTO> products = cart.getCartItems().stream()
	                    .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());

	            cartDTO.setProducts(products);

	            return cartDTO;

	        }).collect(Collectors.toList());

	        cartDTOs.forEach(cart -> cartService.updateProductInCarts(cart.getCartId(), productId));

		return modelMapper.map(updatedProduct, ProductDTO.class);
	}


	@Override
	public ProductDTO deleteProductById(Long productId) {
	Product product =	productRepository.findById(productId)
		.orElseThrow(() -> new ResourceNotFoundException("Product" ,"product ID", productId));
	
	// deleting a product from all the carts when deleted
	List<Cart> carts = cartRepository.findCartsByProductId(productId);
	carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartId(), productId));
	
	productRepository.deleteById(productId);
		
		return modelMapper.map(product, ProductDTO.class);
	}


	@Override
	public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
		// Get product from DB
		Product productFromDB = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "product id", productId));

		String fileName = fileService.uploadImage(path, image);
		
		// Updating the new file name to the product
		productFromDB.setImage(fileName);
		Product updatedProduct = productRepository.save(productFromDB);
		// return DTO after mapping product to DTO
		return modelMapper.map(updatedProduct, ProductDTO.class);
		
	}

	

	
	
	
	
	
	
}
