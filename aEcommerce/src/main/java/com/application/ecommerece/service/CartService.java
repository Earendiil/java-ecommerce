package com.application.ecommerece.service;

import java.util.List;

import com.application.ecommerece.payload.CartDTO;


public interface CartService {

	CartDTO addProductToCart(Long productId, Integer quantity);

	List<CartDTO> getAllCarts();

	CartDTO getCart(String emailId, Long cartId);

	CartDTO updateProductQuantityInCart(Long productId, Integer quantity);

	String deleteProductFromCart(Long cartId, Long productId);

	public void updateProductInCarts(Long cartId, Long productId);

	
}
