package com.application.ecommerece.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.ecommerece.entity.Cart;
import com.application.ecommerece.payload.CartDTO;
import com.application.ecommerece.repositories.CartRepository;
import com.application.ecommerece.service.CartService;
import com.application.ecommerece.util.AuthUtil;

@RestController
@RequestMapping("/api")
public class CartController {

	@Autowired
	private CartService cartService;
	@Autowired
	private AuthUtil authUtil;
	@Autowired
	private CartRepository cartRepository;
	
	@PostMapping("/carts/products/{productId}/quantity/{quantity}")
	public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId,
													@PathVariable Integer quantity){
		
		CartDTO cartDTO = cartService.addProductToCart(productId, quantity);											
		
		return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.CREATED);
	}
	
	@GetMapping("/carts")
	public ResponseEntity<List<CartDTO>> getCarts(){
		
		List<CartDTO>  cartDTOs = cartService.getAllCarts();
		return new ResponseEntity<List<CartDTO>>(cartDTOs, HttpStatus.OK);
	}

	
  @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getCartById(){
	  
        String emailId = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(emailId);
        Long cartId = cart.getCartId();
        
        // we use the cartId also because maybe in the future we have more than
    	// one cart per user. It's more scalable
        CartDTO cartDTO = cartService.getCart(emailId, cartId);
        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);
    }


  @PutMapping("/cart/products/{productId}/quantity/{operation}")
	public ResponseEntity<CartDTO> updateCartProduct(@PathVariable Long productId,
													@PathVariable String operation){
	  CartDTO cartDTO = cartService.updateProductQuantityInCart(productId,
			  	operation.equalsIgnoreCase("delete") ? -1 : 1);
	  return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);
  }
	
  	@DeleteMapping("/carts/{cartId}/product/{productId}")
	public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId,
														@PathVariable Long productId){
	
  		String status = cartService.deleteProductFromCart(cartId, productId);
		return new ResponseEntity<String> (status, HttpStatus.OK);
		
	}
	

		
		
		
	
	
	
	
	
	
	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

