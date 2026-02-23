package com.gcu.data.dao;


import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import com.gcu.data.repository.MovieListRow;

@Repository
public class MovieListDao {
    private final NamedParameterJdbcTemplate jdbc;
    public MovieListDao(NamedParameterJdbcTemplate jdbc) { this.jdbc = jdbc; }


	private static final String SQL = """
	  SELECT m.id, m.movie_name,
	         CONCAT(d.director_first_name,' ', d.director_last_name) AS director,
	         r.rating, vt.`type` AS video_type
	  FROM album_movie am
	  JOIN albums a     ON a.id = am.albums_id
	  JOIN `user` u     ON u.id = a.user_id
	  JOIN login l      ON l.id = u.login_id
	  JOIN movie m      ON m.id = am.movie_id
	  JOIN director d   ON d.id = m.director_id
	  JOIN rating r     ON r.id = m.rating_id
	  JOIN video_type vt ON vt.id = m.video_type_id
	  WHERE l.username = :username AND a.id = :albumId
	  ORDER BY m.id
	""";
	
	public List<MovieListRow> findMoviesForUsername(String username, Long albumId) 
	{
	    return jdbc.query(SQL,
	        Map.of("username", username, "albumId", albumId),
	        (rs, i) -> new MovieListRow(
	            rs.getLong("id"),
	            rs.getString("movie_name"),
	            rs.getString("director"),
	            rs.getString("rating"),
	            rs.getString("video_type")));
    }
}
