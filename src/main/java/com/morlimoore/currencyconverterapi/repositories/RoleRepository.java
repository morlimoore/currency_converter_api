package com.morlimoore.currencyconverterapi.repositories;

import com.morlimoore.currencyconverterapi.entities.Role;
import com.morlimoore.currencyconverterapi.util.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(RoleEnum name);
}