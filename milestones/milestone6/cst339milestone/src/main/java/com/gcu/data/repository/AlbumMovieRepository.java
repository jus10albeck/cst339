package com.gcu.data.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gcu.data.entity.AlbumMovieEntity;

public interface AlbumMovieRepository extends CrudRepository<AlbumMovieEntity, Long>
{

	@Query("""
        SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END
        FROM album_movie am
        JOIN albums a   ON a.id = am.albums_id
        JOIN `user` u   ON u.id = a.user_id
        JOIN login l    ON l.id = u.login_id
        JOIN movie m    ON m.id = am.movie_id
        WHERE l.username = :username
          AND a.id = :albumId
          AND LOWER(m.movie_name) = LOWER(:movieName)
    """)
    boolean existsMovieNameInAlbum(@Param("username") String username,
                                   @Param("albumId")   Long albumId,
                                   @Param("movieName") String movieName);


	@Query("""
	        SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END
	        FROM album_movie am
	        JOIN albums a   ON a.id = am.albums_id
	        JOIN `user` u   ON u.id = a.user_id
	        JOIN login l    ON l.id = u.login_id
	        WHERE l.username = :username
	          AND am.movie_id = :movieId
	    """)
	    boolean userOwnsMovie(@Param("username") String username, @Param("movieId") Long movieId);

	    @Modifying
	    @Query("""
	        INSERT INTO album_movie (albums_id, movie_id, added_time)
	        VALUES (:albumId, :movieId, NOW())
	    """)
	    int addMovieToAlbum(@Param("albumId") Long albumId, @Param("movieId") Long movieId);

	    @Modifying
	    @Query("""
	        DELETE am FROM album_movie am
	        JOIN albums a ON a.id = am.albums_id
	        JOIN `user`  u ON u.id = a.user_id
	        JOIN login  l ON l.id = u.login_id
	        WHERE l.username = :username AND a.id = :albumId AND am.movie_id = :movieId
	    		""")
    int deleteFromUserAlbum(@Param("username") String username,
                            @Param("albumId") Long albumId,
                            @Param("movieId") Long movieId);

    @Query("SELECT COUNT(*) FROM album_movie WHERE movie_id = :movieId")
    int countAlbumLinksForMovie(@Param("movieId") Long movieId);
}
