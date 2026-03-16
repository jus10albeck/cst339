package com.gcu.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("video_type")
public class VideoTypeEntity
{
	@Id
	private Long id;
	@Column("type")
	private String type;

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
	public String getType()
	{
		return type;
	}

	/**
	 * setters
	 * @param id
	 */
	public void setType(String type)
	{
		this.type = type;
	}
}
