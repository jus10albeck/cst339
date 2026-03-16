package com.gcu.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("movie")
public class MovieEntity
{
	@Id
	private Long id;
	
	@Column("movie_name")
	private String movieName;
	
	@Column("director_id")
	private Long directorId;
	
	@Column("rating_id")
	private Long ratingId;
	
	@Column("video_type_id")
	private Long videoTypeId;

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
	public String getMovieName()
	{
		return movieName;
	}

	/**
	 * setters
	 * @param id
	 */
	public void setMovieName(String movieName)
	{
		this.movieName = movieName;
	}

	/**
	 * getters
	 * @return
	 */
	public Long getDirectorId()
	{
		return directorId;
	}

	/**
	 * setters
	 * @param id
	 */
	public void setDirectorId(Long directorId)
	{
		this.directorId = directorId;
	}

	/**
	 * getters
	 * @return
	 */
	public Long getRatingId()
	{
		return ratingId;
	}

	/**
	 * setters
	 * @param id
	 */
	public void setRatingId(Long ratingId)
	{
		this.ratingId = ratingId;
	}

	/**
	 * getters
	 * @return
	 */
	public Long getVideoTypeId()
	{
		return videoTypeId;
	}

	/**
	 * setters
	 * @param id
	 */
	public void setVideoTypeId(Long videoTypeId)
	{
		this.videoTypeId = videoTypeId;
	}
}
