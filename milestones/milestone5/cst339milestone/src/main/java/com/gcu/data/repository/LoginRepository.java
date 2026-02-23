package com.gcu.data.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.gcu.data.entity.LoginEntity;

public interface LoginRepository extends CrudRepository<LoginEntity, Long>
{
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
	
	Optional<LoginEntity> findByUsername(String username);
}
