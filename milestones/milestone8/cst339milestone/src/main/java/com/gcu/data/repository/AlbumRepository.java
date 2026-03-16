package com.gcu.data.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gcu.data.entity.AlbumEntity;


public interface AlbumRepository extends CrudRepository<AlbumEntity, Long>
{
    @Query("""
        SELECT a.* FROM albums a
        JOIN `user` u ON u.id = a.user_id
        JOIN login l ON l.id = u.login_id
        WHERE l.username_hash = :usernameHash
        ORDER BY a.name ASC
    """)
    List<AlbumEntity> findAllForUsernameHash(@Param("usernameHash") byte[] usernameHash);

    @Query("""
        SELECT a.id FROM albums a
        JOIN `user` u ON u.id = a.user_id
        JOIN login l ON l.id = u.login_id
        WHERE l.username_hash = :usernameHash
        ORDER BY a.id
        LIMIT 1
    """)
    Optional<Long> findPrimaryAlbumIdByUsernameHash(@Param("usernameHash") byte[] usernameHash);
}