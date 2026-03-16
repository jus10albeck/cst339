package com.gcu.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LoginModel 
{
	@NotNull(message="Username is a required field")
	@Size(min=1, max=32, message="Username must be between 1 and 32 characters")
	private String username;
	
	@NotNull(message="Password is a required field")
	@Size(min=1, max=32, message="Password must be between 1 and 32 characters")
	private String password;
	
	/**
	 * default constructor
	 */
	public LoginModel(){}
	
	/**
	 * constructor with parameters
	 * @param username
	 * @param password
	 */
	public LoginModel(String username, String password)
	{
		this.username = username;
		this.password = password;
	}
	
	/**
	 * getters
	 * @return
	 */
	public String getUsername()
	{
		return username;
	}
	
	/**
	 * setters
	 * @param username
	 */
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	/**
	 * getters
	 * @return
	 */
	public String getPassword()
	{
		return password;
	}
	
	/**
	 * setters
	 * @param password
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}
}