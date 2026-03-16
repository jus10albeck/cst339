package com.gcu.api;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.gcu.api.dto.ProductDto;

@Repository
public class ProductReadDao
{
	NamedParameterJdbcTemplate jdbc;
	
	public ProductReadDao(NamedParameterJdbcTemplate jdbc)
	{
		this.jdbc = jdbc;
	}
	

	private static final String BASE_SELECT = """
        SELECT m.id,
               m.movie_name,
               CONCAT(d.director_first_name, ' ', d.director_last_name) AS director,
               r.rating,
               vt.`type` AS video_type
        FROM movie m
        JOIN director d   ON d.id = m.director_id
        JOIN rating r     ON r.id = m.rating_id
        JOIN video_type vt ON vt.id = m.video_type_id
        """;
	

	public List<ProductDto> findAll() 
	{
        String sql = BASE_SELECT + " ORDER BY m.id";
        return jdbc.query(sql, (rs, i) -> new ProductDto(
                rs.getLong("id"),
                rs.getString("movie_name"),
                rs.getString("director"),
                rs.getString("rating"),
                rs.getString("video_type")));
    }

    public ProductDto findById(Long id) 
    {
        String sql = BASE_SELECT + " WHERE m.id = :id";
        var list = jdbc.query(sql, Map.of("id", id), (rs, i) -> new ProductDto(
                rs.getLong("id"),
                rs.getString("movie_name"),
                rs.getString("director"),
                rs.getString("rating"),
                rs.getString("video_type")));
        return list.isEmpty() ? null : list.get(0);
    }
}
