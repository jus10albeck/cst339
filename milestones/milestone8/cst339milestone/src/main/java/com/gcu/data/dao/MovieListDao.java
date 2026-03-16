package com.gcu.data.dao;


import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import com.gcu.data.repository.MovieListRow;
import com.gcu.data.repository.WishlistRow;


@Repository
public class MovieListDao {

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Constructor for MovieListDao. It initializes the DAO with a NamedParameterJdbcTemplate 
     * which is used to execute SQL queries against the database.
     * @param jdbc
     */
    public MovieListDao(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final String SQL = """
         SELECT m.id,
                m.movie_name,
                CONCAT(d.director_first_name,' ', d.director_last_name) AS director,
                r.rating,
                vt.`type` AS video_type
         FROM album_movie am
         JOIN albums    a  ON a.id = am.albums_id
         JOIN movie     m  ON m.id = am.movie_id
         JOIN director  d  ON d.id = m.director_id
         JOIN rating    r  ON r.id = m.rating_id
         JOIN video_type vt ON vt.id = m.video_type_id
         WHERE a.id = :albumId
         ORDER BY m.id
         """;

    /**
     * Finds movies for a given username and album ID. It executes a SQL query to 
     * retrieve the list of movies associated with the specified album and maps the 
     * result set to a list of MovieListRow objects. The query joins the album_movie, 
     * albums, movie, director, rating, and video_type tables to gather the necessary 
     * information about each movie in the album. The results are ordered by movie ID.    
     * @param username
     * @param albumId
     * @return
     */
    public List<MovieListRow> findMoviesForUsername(String username, Long albumId) 
    {
        return jdbc.query(
            SQL,
            Map.of("albumId", albumId),
            (rs, i) -> new MovieListRow(
                rs.getLong("id"),
                rs.getString("movie_name"),
                rs.getString("director"),
                rs.getString("rating"),
                rs.getString("video_type")));
    }
    

	private static final String WISHLIST_SQL = """
	        SELECT
	          w.id                           AS id,
	          m.id                           AS movie_id,
	          m.movie_name                   AS movie_name,
	          CONCAT(d.director_first_name,' ', d.director_last_name) AS director,
	          r.rating                       AS rating,
	          vt.`type`                      AS video_type
	        FROM wishlist w
	        JOIN `user` u    ON u.id = w.user_id
	        JOIN login l     ON l.id = u.login_id
	        JOIN movie m     ON m.id = w.movie_id
	        JOIN director d  ON d.id = m.director_id
	        JOIN rating r    ON r.id = m.rating_id
	        JOIN video_type vt ON vt.id = m.video_type_id
	        WHERE l.username_hash = :usernameHash
	        ORDER BY w.id DESC
	        """;

    /**
     * Finds the wishlist for a user based on their username hash. It executes a SQL query to 
     * retrieve the list of movies in the user's wishlist and maps the result set to a list 
     * of WishlistRow objects. The query joins the wishlist, user, login, movie, director, 
     * rating, and video_type tables to gather the necessary information about each movie in 
     * the user's wishlist. The results are ordered by wishlist ID in descending order.      
     * @param usernameHash
     * @return
     */
    public List<WishlistRow>
    findWishlistForUser(byte[] usernameHash) 
    {
        return jdbc.query(
            WISHLIST_SQL,
            java.util.Map.of("usernameHash", usernameHash),
            (rs, i) -> new com.gcu.data.repository.WishlistRow(
                rs.getLong("id"),
                rs.getLong("movie_id"),
                rs.getString("movie_name"),
                rs.getString("director"),
                rs.getString("rating"),
                rs.getString("video_type")
            )
        );
    }
}