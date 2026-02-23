package com.gcu.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("login")
public class LoginEntity
{
	@Id
	private Long id;
	private String email;
	private String username;
	private String password;
	
	public LoginEntity() {}
	
	public LoginEntity(Long id, String email, String username, String password)
	{
		this.setId(id);
		this.setEmail(email);
		this.setUsername(username);
		this.setPassword(password);
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}
}
