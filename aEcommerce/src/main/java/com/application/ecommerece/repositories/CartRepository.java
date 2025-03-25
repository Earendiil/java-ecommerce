package com.application.ecommerece.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.application.ecommerece.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long>{

	@Query("SELECT c FROM Cart c WHERE c.user.email = ?1")
	Cart  findCartByEmail(String email);

	 @Query("SELECT c FROM Cart c WHERE c.user.email = ?1 AND c.id = ?2")
	Cart findByEmailAndCartId(String emailId, Long cartId);

	Cart findByCartId(Long cartId);

	 @Query("SELECT c FROM Cart c JOIN FETCH c.cartItems ci JOIN FETCH ci.product p WHERE p.id = ?1")
	    List<Cart> findCartsByProductId(Long productId);
}
