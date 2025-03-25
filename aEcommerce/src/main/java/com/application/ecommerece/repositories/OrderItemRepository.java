package com.application.ecommerece.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.ecommerece.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

}
