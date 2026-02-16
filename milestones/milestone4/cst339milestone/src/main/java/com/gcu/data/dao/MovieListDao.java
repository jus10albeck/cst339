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
        SELECT 
          m.id,
          m.movie_name,
          CONCAT(d.director_first_name, ' ', d.director_last_name) AS director,
          r.rating,
          vt.`type` AS video_type
        FROM collection c
        JOIN `user` u   ON u.id = c.user_id
        JOIN login l    ON l.id = u.login_id
        JOIN movie m    ON m.id = c.movie_id
        JOIN director d ON d.id = m.director_id
        JOIN rating r   ON r.id = m.rating_id
        JOIN video_type vt ON vt.id = m.video_type_id
        WHERE l.username = :username
        ORDER BY m.id
        """;

    public List<MovieListRow> findMoviesForUsername(String username) {
        return jdbc.query(SQL, Map.of("username", username), (rs, i) -> {
            return new MovieListRow(
                rs.getLong("id"),
                rs.getString("movie_name"),
                rs.getString("director"),
                rs.getString("rating"),
                rs.getString("video_type")
            );
        });
    }
}
