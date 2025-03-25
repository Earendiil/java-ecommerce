package com.application.ecommerece.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.ecommerece.entity.Address;

public interface AddressRepository extends JpaRepository<Address,Long>{

}
