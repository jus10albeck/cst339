package com.gcu.data.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gcu.data.entity.CollectionEntity;


public interface CollectionRepository extends CrudRepository<CollectionEntity, Long>
{
    @Query("""
         SELECT
           m.id AS id,
           m.movie_name AS movieName,
           CONCAT(d.director_first_name, ' ', d.director_last_name) AS director,
           r.rating AS rating,
           vt.`type` AS videoType
         FROM collection c
         JOIN `user` u ON u.id = c.user_id
         JOIN login l ON l.id = u.login_id
         JOIN movie m ON m.id = c.movie_id
         JOIN director d ON d.id = m.director_id
         JOIN rating r ON r.id = m.rating_id
         JOIN video_type vt ON vt.id = m.video_type_id
         WHERE l.username_hash = :usernameHash
         ORDER BY m.id
    """)
    List<MovieListRow> findMoviesForUsernameHash(@Param("usernameHash") byte[] usernameHash);

    @Query("""
        SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END
        FROM collection c
        JOIN `user` u ON u.id = c.user_id
        JOIN login l ON l.id = u.login_id
        JOIN movie m ON m.id = c.movie_id
        WHERE l.username_hash = :usernameHash
          AND LOWER(m.movie_name) = LOWER(:movieName)
    """)
    boolean existsMovieNameForUser(@Param("usernameHash") byte[] usernameHash,
                                   @Param("movieName") String movieName);

    @Query("""
        SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END
        FROM collection c
        JOIN `user` u ON u.id = c.user_id
        JOIN login l ON l.id = u.login_id
        JOIN movie m ON m.id = c.movie_id
        JOIN director d ON d.id = m.director_id
        WHERE l.username_hash = :usernameHash
          AND LOWER(m.movie_name) = LOWER(:movieName)
          AND LOWER(CONCAT(d.director_first_name, ' ', d.director_last_name)) = LOWER(:director)
    """)
    boolean existsMovieNameAndDirectorForUser(@Param("usernameHash") byte[] usernameHash,
                                              @Param("movieName") String movieName,
                                              @Param("director") String director);

    @Query("""
        SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END
        FROM collection c
        JOIN `user` u ON u.id = c.user_id
        JOIN login l ON l.id = u.login_id
        WHERE l.username_hash = :usernameHash
          AND c.movie_id = :movieId
    """)
    boolean userOwnsMovie(@Param("usernameHash") byte[] usernameHash,
                          @Param("movieId") Long movieId);

    @Modifying
    @Query("""
        DELETE c FROM collection c
        JOIN `user` u ON u.id = c.user_id
        JOIN login l ON l.id = u.login_id
        WHERE l.username_hash = :usernameHash
          AND c.movie_id = :movieId
    """)
    int deleteFromUserCollection(@Param("usernameHash") byte[] usernameHash,
                                 @Param("movieId") Long movieId);

    @Query("""
        SELECT COUNT(*) FROM collection WHERE movie_id = :movieId
    """)
    int countCollectionsForMovie(@Param("movieId") Long movieId);
}