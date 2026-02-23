package com.gcu.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class MovieModel 
{
	private Long id;
	
	@NotBlank(message = "Movie name is required")
	@Size(max = 100, message = "Movie name must be less than 100 characters.")
	private String movieName;
	
	@NotBlank(message ="Director is required")
	@Size(max = 50, message="Director name must be less than 50 characters")
	private String director;
	
	@Pattern(regexp = "^(G|PG|PG-13|R)$", message = "Rating must be one of G, PG, PG-13, R")
	private String rating;
	
	@Pattern(regexp = "^(DVD|Blu-Ray|VHS)$", message = "Video type must be one of DVD, Blu-Ray, or VHS")
	private String videoType;
	
	public MovieModel() {}
	
	public MovieModel(Long id, String movieName, String director, String rating, String videoType)
	{
		this.setId(id);
		this.setMovieName(movieName);
		this.setDirector(director);
		this.setRating(rating);
		this.setVideoType(videoType);
	}

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

	public String getDirector() 
	{
		return director;
	}

	public void setDirector(String director) 
	{
		this.director = director;
	}

	public String getRating() 
	{
		return rating;
	}

	public void setRating(String rating) 
	{
		this.rating = rating;
	}

	public String getVideoType() 
	{
		return videoType;
	}

	public void setVideoType(String videoType) 
	{
		this.videoType = videoType;
	}
}
