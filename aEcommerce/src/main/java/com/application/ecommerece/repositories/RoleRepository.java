package com.application.ecommerece.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.ecommerece.entity.AppRole;
import com.application.ecommerece.entity.Role;

public interface RoleRepository extends JpaRepository <Role, Long>{

	Optional<Role> findByRoleName(AppRole roleUser);

}
