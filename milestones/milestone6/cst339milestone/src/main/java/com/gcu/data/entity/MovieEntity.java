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

	public Long getId()
	{
		return id;
	}


	public void setId(Long id)
	{
		this.id = id;
	}


	public String getMovieName()
	{
		return movieName;
	}


	public void setMovieName(String movieName)
	{
		this.movieName = movieName;
	}


	public Long getDirectorId()
	{
		return directorId;
	}


	public void setDirectorId(Long directorId)
	{
		this.directorId = directorId;
	}


	public Long getRatingId()
	{
		return ratingId;
	}


	public void setRatingId(Long ratingId)
	{
		this.ratingId = ratingId;
	}


	public Long getVideoTypeId()
	{
		return videoTypeId;
	}


	public void setVideoTypeId(Long videoTypeId)
	{
		this.videoTypeId = videoTypeId;
	}
}
