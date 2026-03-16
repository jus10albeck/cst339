package com.gcu.data.repository;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gcu.data.entity.LoginEntity;


public interface LoginRepository extends CrudRepository<LoginEntity, Long> 
{

    boolean existsByEmail(String email);

    @org.springframework.data.jdbc.repository.query.Query(
        "SELECT * FROM login WHERE username_hash = :h LIMIT 1")
    java.util.Optional<LoginEntity> findByUsernameHash(
        @org.springframework.data.repository.query.Param("h") byte[] h);

    @org.springframework.data.jdbc.repository.query.Query(
        "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM login WHERE username_hash = :h")
    boolean existsByUsernameHash(
        @org.springframework.data.repository.query.Param("h") byte[] h);
}