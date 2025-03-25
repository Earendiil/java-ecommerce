package com.application.ecommerce.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.ecommerce.entity.AppRole;
import com.application.ecommerce.entity.Role;

public interface RoleRepository extends JpaRepository <Role, Long>{

	Optional<Role> findByRoleName(AppRole roleUser);

}
