package com.gcu.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserRegistrationModel 
{
	@NotNull(message="First Name is a required field")
	@Size(min=1, max=32, message="First name must be between 1 and 32 characters")
	private String firstName;
	
	@NotNull(message="Last Name is a required field")
	@Size(min=1, max=32, message="Last name must be between 1 and 32 characters")
	private String lastName;
	
	@NotNull(message="Email is a required field")
	@Size(min=1, max=32, message="Email must be between 1 and 32 characters")
	private String email;

	@NotNull(message="Username is a required field")
	@Size(min=1, max=32, message="Username must be between 1 and 32 characters")
	private String username;
	
	@NotNull(message="Password is a required field")
	@Size(min=1, max=32, message="Password must be between 1 and 32 characters")
	private String password;
		
	/**
	 * getter
	 * @return
	 */
	public String getFirstName() 
	{
		return firstName;
	}
	
	/**
	 * setter
	 * @param firstName
	 */
	public void setFirstName(String firstName) 
	{
		this.firstName = firstName;
	}
	
	/**
	 * getter
	 * @return
	 */
	public String getLastName() 
	{
		return lastName;
	}

	/**
	 * setter
	 * @param lastName
	 */
	public void setLastName(String lastName) 
	{
		this.lastName = lastName;
	}
	
	/**
	 * getter
	 * @return
	 */
	public String getEmail() 
	{
		return email;
	}
	
	/**
	 * setter
	 * @param email
	 */
	public void setEmail(String email) 
	{
		this.email = email;
	}

	/**
	 * getter
	 * @return
	 */
	public String getUsername() 
	{
		return username;
	}
	
	/**
	 * setter
	 * @param username
	 */
	public void setUsername(String username) 
	{
		this.username = username;
	}
	
	/**
	 * getter
	 * @return
	 */
	public String getPassword() 
	{
		return password;
	}
	
	/**
	 * setter
	 * @param password
	 */
	public void setPassword(String password) 
	{
		this.password = password;
	}
}
