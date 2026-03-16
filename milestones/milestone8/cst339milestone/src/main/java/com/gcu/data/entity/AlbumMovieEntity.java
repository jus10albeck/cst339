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

	/**
	 * getters
	 * @return
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * setters
	 * @param id
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * getters
	 * @return
	 */
	public Long getAlbumId()
	{
		return albumId;
	}

	/**
	 * setters
	 * @param id
	 */
	public void setAlbumId(Long albumId)
	{
		this.albumId = albumId;
	}

	/**
	 * getters
	 * @return
	 */
	public Long getMovieId()
	{
		return movieId;
	}

	/**
	 * setters
	 * @param id
	 */
	public void setMovieId(Long movieId)
	{
		this.movieId = movieId;
	}

	/**
	 * getters
	 * @return
	 */
	public LocalDateTime getAddedTime()
	{
		return addedTime;
	}

	/**
	 * setters
	 * @param id
	 */
	public void setAddedTime(LocalDateTime addedTime)
	{
		this.addedTime = addedTime;
	}
}
