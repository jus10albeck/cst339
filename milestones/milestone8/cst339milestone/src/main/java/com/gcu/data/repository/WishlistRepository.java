package com.gcu.data.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gcu.data.entity.WishlistEntity;


public interface WishlistRepository extends CrudRepository<WishlistEntity, Long>
{
    @Query("""
        SELECT
          w.id AS id,
          m.id AS movieId,
          m.movie_name AS movieName,
          CONCAT(d.director_first_name,' ', d.director_last_name) AS director,
          r.rating AS rating,
          vt.`type` AS videoType
        FROM wishlist w
        JOIN `user` u ON u.id = w.user_id
        JOIN login l  ON l.id = u.login_id
        JOIN movie m  ON m.id = w.movie_id
        JOIN director d ON d.id = m.director_id
        JOIN rating r   ON r.id = m.rating_id
        JOIN video_type vt ON vt.id = m.video_type_id
        WHERE l.username_hash = :usernameHash
        ORDER BY w.id DESC
    """)
    List<WishlistRow> findWishlistForUser(@Param("usernameHash") byte[] usernameHash);

    @Query("""
        SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END
        FROM wishlist w
        JOIN `user` u ON u.id = w.user_id
        JOIN login l  ON l.id = u.login_id
        WHERE l.username_hash = :usernameHash AND w.movie_id = :movieId
    """)
    boolean existsInWishlist(@Param("usernameHash") byte[] usernameHash,
                             @Param("movieId") Long movieId);

    @Modifying
    @Query("""
        INSERT INTO wishlist (user_id, movie_id)
        VALUES (:userId, :movieId)
    """)
    int insert(@Param("userId") Long userId,
               @Param("movieId") Long movieId);

    @Modifying
    @Query("""
        DELETE w FROM wishlist w
        JOIN `user` u ON u.id = w.user_id
        JOIN login l  ON l.id = u.login_id
        WHERE l.username_hash = :usernameHash AND w.movie_id = :movieId
    """)
    int deleteFromWishlist(@Param("usernameHash") byte[] usernameHash,
                           @Param("movieId") Long movieId);

    Optional<WishlistEntity> findById(Long id);
}
