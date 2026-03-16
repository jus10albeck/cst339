package com.gcu.data.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("albums")
public class AlbumEntity
{
	@Id
	private Long id;
	private String name;
	private String description;	
	@Column("updated_time")
	private LocalDateTime updatedTime;	
	@Column("user_id")
	private Long userId;

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
	public String getName()
	{
		return name;
	}

	/**
	 * setters
	 * @param id
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * getters
	 * @return
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * setters
	 * @param id
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * getters
	 * @return
	 */
	public LocalDateTime getUpdatedTime()
	{
		return updatedTime;
	}

	/**
	 * setters
	 * @param id
	 */
	public void setUpdatedTime(LocalDateTime updatedTime)
	{
		this.updatedTime = updatedTime;
	}

	/**
	 * getters
	 * @return
	 */
	public Long getUserId()
	{
		return userId;
	}

	/**
	 * setters
	 * @param id
	 */
	public void setUserId(Long userId)
	{
		this.userId = userId;
	}	
}
