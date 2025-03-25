package com.application.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.ecommerce.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

}
