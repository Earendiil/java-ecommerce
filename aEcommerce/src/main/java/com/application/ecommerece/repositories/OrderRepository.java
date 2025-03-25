package com.application.ecommerece.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.ecommerece.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
