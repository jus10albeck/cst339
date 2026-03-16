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
	
	/**
	 * default constructor
	 */
	public MovieModel() {}
	
	/**
	 * constructor with parameters
	 * @param id
	 * @param movieName
	 * @param director
	 * @param rating
	 * @param videoType
	 */
	public MovieModel(Long id, String movieName, String director, String rating, String videoType)
	{
		this.setId(id);
		this.setMovieName(movieName);
		this.setDirector(director);
		this.setRating(rating);
		this.setVideoType(videoType);
	}

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
	 * @param movieName
	 */
	public void setMovieName(String movieName) 
	{
		this.movieName = movieName;
	}

	/**
	 * getters
	 * @return
	 */
	public String getDirector() 
	{
		return director;
	}

	/**
	 * setters
	 * @param director
	 */
	public void setDirector(String director) 
	{
		this.director = director;
	}

	/**
	 * getters
	 * @return
	 */
	public String getRating() 
	{
		return rating;
	}

	/**
	 * setters
	 * @param rating
	 */
	public void setRating(String rating) 
	{
		this.rating = rating;
	}

	/**
	 * getters
	 * @return
	 */
	public String getVideoType() 
	{
		return videoType;
	}

	/**
	 * setters
	 * @param videoType
	 */
	public void setVideoType(String videoType) 
	{
		this.videoType = videoType;
	}
}
