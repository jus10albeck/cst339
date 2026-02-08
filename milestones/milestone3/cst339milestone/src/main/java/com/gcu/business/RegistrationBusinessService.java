package com.gcu.business;

import org.springframework.stereotype.Service;

import com.gcu.model.UserRegistrationModel;

@Service
public class RegistrationBusinessService 
{
	public boolean register(UserRegistrationModel model)
	{
		//Emulate registration success (There is no database)
		System.out.println("Hello from RegistrationsBusinessService!");
		return true;
	}
}
