package com.gcu.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("collection")
public class CollectionEntity
{
	@Id
	private Long id;
	
	@Column("user_id")
	private Long userId;
	
	@Column("movie_id")
	private Long movieId;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public Long getMovieId()
	{
		return movieId;
	}

	public void setMovieId(Long movieId)
	{
		this.movieId = movieId;
	}
	
	
}
