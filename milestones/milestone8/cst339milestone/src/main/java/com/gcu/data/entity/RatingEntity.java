package com.gcu.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("rating")
public class RatingEntity
{
	@Id
	private Long id;
	
	private String rating;

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
	public String getRating()
	{
		return rating;
	}

	/**
	 * setters
	 * @param id
	 */
	public void setRating(String rating)
	{
		this.rating = rating;
	}
}
