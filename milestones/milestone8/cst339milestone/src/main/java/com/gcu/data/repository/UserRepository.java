package com.gcu.data.repository;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gcu.data.entity.UserEntity;


public interface UserRepository extends CrudRepository<UserEntity, Long>
{
    @Query("""
            SELECT u.id
            FROM `user` u
            JOIN login l ON l.id = u.login_id
            WHERE l.username_hash = :usernameHash
            """)
    Optional<Long> findUserIdByUsernameHash(@Param("usernameHash") byte[] usernameHash);
}