package com.application.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.ecommerce.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
