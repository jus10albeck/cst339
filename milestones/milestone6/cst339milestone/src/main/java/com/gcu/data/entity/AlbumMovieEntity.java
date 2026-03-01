package com.gcu.data.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("album_movie")
public class AlbumMovieEntity
{
	@Id
	private Long id;	
	@Column("albums_id")
	private Long albumId;	
	@Column("movie_id")
	private Long movieId;	
	@Column("added_time")
	private LocalDateTime addedTime;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getAlbumId()
	{
		return albumId;
	}

	public void setAlbumId(Long albumId)
	{
		this.albumId = albumId;
	}

	public Long getMovieId()
	{
		return movieId;
	}

	public void setMovieId(Long movieId)
	{
		this.movieId = movieId;
	}

	public LocalDateTime getAddedTime()
	{
		return addedTime;
	}

	public void setAddedTime(LocalDateTime addedTime)
	{
		this.addedTime = addedTime;
	}
}
