package com.application.ecommerce.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.application.ecommerce.entity.Cart;
import com.application.ecommerce.entity.Category;
import com.application.ecommerce.entity.Product;
import com.application.ecommerce.exceptions.APIException;
import com.application.ecommerce.exceptions.ResourceNotFoundException;
import com.application.ecommerce.payload.CartDTO;
import com.application.ecommerce.payload.ProductDTO;
import com.application.ecommerce.payload.ProductResponse;
import com.application.ecommerce.repositories.CartRepository;
import com.application.ecommerce.repositories.CategoryRepository;
import com.application.ecommerce.repositories.ProductRepository;

import jakarta.persistence.criteria.CriteriaBuilder;

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
	@Value("${image.base.url}")
	private String imageBaseUrl;
	

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
	public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String category, String keyword) {
		Sort sortByAndOrder = Optional.ofNullable(sortOrder)
		        .map(order -> order.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending())
		        .orElse(Sort.by(sortBy).ascending());

		
		PageRequest pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		Specification<Product> spec = Specification.where(null);
		if (keyword != null && !keyword.isEmpty()) {
			spec = spec.and((root, query, criteriaBuilder) ->
	        criteriaBuilder.like(criteriaBuilder.lower(root.get("productName")),
	            "%" + keyword.toLowerCase() + "%"));
		}
		if (category != null && !category.isEmpty()) {
			spec = spec.and((root, query, criteriaBuilder) -> 
			 criteriaBuilder.equal(root.get("category").get("categoryName"), category));
		}
		
		
		Page<Product> pageProducts = productRepository.findAll(spec, pageDetails);
		
		List<Product> products = pageProducts.getContent();
		
		if (products.isEmpty())
			throw new APIException("No products exist!");
		
		List<ProductDTO> productDTOS = products.stream()
				.map(product -> {
				ProductDTO productDTO =	modelMapper.map(product, ProductDTO.class);
				productDTO.setImage(constructImageUrl(product.getImage()));
				return productDTO;
				})
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
	 private String constructImageUrl(String imageName) {
		 return imageBaseUrl.endsWith("/") ? imageName + imageName :imageBaseUrl + "/" + imageName;
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
	            .orElseThrow(() -> new ResourceNotFoundException("Product", "product name", productId));

	    Product product = modelMapper.map(productDTO, Product.class);

	    updatedProduct.setDescription(product.getDescription());
	    updatedProduct.setDiscount(product.getDiscount());
	    updatedProduct.setPrice(product.getPrice());
	    updatedProduct.setProductName(product.getProductName());
	    updatedProduct.setQuantity(product.getQuantity());
	    double specialPrice = updatedProduct.getPrice() - ((updatedProduct.getDiscount() * 0.01) * updatedProduct.getPrice());
	    updatedProduct.setSpecialPrice(specialPrice);

	    if (productDTO.getImage() != null) {
	        updatedProduct.setImage(productDTO.getImage());
	    }
	    productRepository.save(updatedProduct);

	    List<Cart> carts = cartRepository.findCartsByProductId(productId);
	    List<CartDTO> cartDTOs = carts.stream().map(cart -> {
	        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
	        List<ProductDTO> products = cart.getCartItems().stream()
	                .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class))
	                .collect(Collectors.toList());
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
