package com.gcu.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("director")
public class DirectorEntity
{
	@Id
	private Long id;
	
	@Column("director_first_name")
	private String directorFirstName;
	
	@Column("director_last_name")
	private String directorLastName;

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
	public String getDirectorFirstName()
	{
		return directorFirstName;
	}

	/**
	 * setters
	 * @param id
	 */
	public void setDirectorFirstName(String directorFirstName)
	{
		this.directorFirstName = directorFirstName;
	}

	/**
	 * getters
	 * @return
	 */
	public String getDirectorLastName()
	{
		return directorLastName;
	}

	/**
	 * setters
	 * @param id
	 */
	public void setDirectorLastName(String directorLastName)
	{
		this.directorLastName = directorLastName;
	}
}
