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
		    WHERE l.username = :username
		    ORDER BY m.id
	        """)
	    List<MovieListRow> findMoviesForUsername(
	    		@Param("username") 
	    		String username);
	

		@Query("""
			SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END
			FROM collection c
			JOIN `user` u ON u.id = c.user_id
			JOIN login l ON l.id = u.login_id
			JOIN movie m ON m.id = c.movie_id
			WHERE l.username = :username
			  AND LOWER(m.movie_name) = LOWER(:movieName)
		""")
		boolean existsMovieNameForUser(@Param("username") String username,
		                               @Param("movieName") String movieName);

		// stricter duplicate rule: same name + same director
		@Query("""
			SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END
			FROM collection c
			JOIN `user` u ON u.id = c.user_id
			JOIN login l ON l.id = u.login_id
			JOIN movie m ON m.id = c.movie_id
			JOIN director d ON d.id = m.director_id
			WHERE l.username = :username
			  AND LOWER(m.movie_name) = LOWER(:movieName)
			  AND LOWER(CONCAT(d.director_first_name, ' ',
			   d.director_last_name)) = LOWER(:director)
		""")
		boolean existsMovieNameAndDirectorForUser(@Param("username") String username,
		                                          @Param("movieName") String movieName,
		                                          @Param("director") String director);

		// Verify a movie belongs to this user (authorization check for edit/delete)
		@Query("""
			SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END
			FROM collection c
			JOIN `user` u ON u.id = c.user_id
			JOIN login l ON l.id = u.login_id
			WHERE l.username = :username
			  AND c.movie_id = :movieId
		""")
		boolean userOwnsMovie(@Param("username") String username,
		                      @Param("movieId") Long movieId);

		// Remove one link from user's collection
		@Modifying
		@Query("""
			DELETE c FROM collection c
			JOIN `user` u ON u.id = c.user_id
			JOIN login l ON l.id = u.login_id
			WHERE l.username = :username
			  AND c.movie_id = :movieId
		""")
		int deleteFromUserCollection(@Param("username") String username,
		                             @Param("movieId") Long movieId);


		@Query("""
				SELECT COUNT(*) 
				FROM collection 
				WHERE movie_id = :movieId
		""")
		int countCollectionsForMovie(@Param("movieId") Long movieId);
}