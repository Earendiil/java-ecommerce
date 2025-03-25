package com.application.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.ecommerce.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{

}
