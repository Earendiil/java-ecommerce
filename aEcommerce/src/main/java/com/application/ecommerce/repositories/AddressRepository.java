package com.application.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.ecommerce.entity.Address;

public interface AddressRepository extends JpaRepository<Address,Long>{

}
