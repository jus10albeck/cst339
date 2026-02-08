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
	
	@NotNull(message="Phone Number is a required field")
	@Size(min=1, max=10, message="Phone Number must be 10 numbers")
	private String phoneNumber;
	
	public String getFirstName() 
	{
		return firstName;
	}
	
	public void setFirstName(String firstName) 
	{
		this.firstName = firstName;
	}
	
	public String getLastName() 
	{
		return lastName;
	}
	
	public void setLastName(String lastName) 
	{
		this.lastName = lastName;
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
	
	public String getPhoneNumber() 
	{
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) 
	{
		this.phoneNumber = phoneNumber;
	}
}
