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

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public LocalDateTime getUpdatedTime()
	{
		return updatedTime;
	}

	public void setUpdatedTime(LocalDateTime updatedTime)
	{
		this.updatedTime = updatedTime;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}	
}
