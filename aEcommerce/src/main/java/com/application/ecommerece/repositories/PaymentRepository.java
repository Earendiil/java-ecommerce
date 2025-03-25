package com.application.ecommerece.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.ecommerece.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{

}
