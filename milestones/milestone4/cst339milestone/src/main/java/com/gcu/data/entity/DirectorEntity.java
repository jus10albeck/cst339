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


	public Long getId()
	{
		return id;
	}


	public void setId(Long id)
	{
		this.id = id;
	}


	public String getDirectorFirstName()
	{
		return directorFirstName;
	}


	public void setDirectorFirstName(String directorFirstName)
	{
		this.directorFirstName = directorFirstName;
	}


	public String getDirectorLastName()
	{
		return directorLastName;
	}


	public void setDirectorLastName(String directorLastName)
	{
		this.directorLastName = directorLastName;
	}
}
