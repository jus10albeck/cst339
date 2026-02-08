package com.gcu.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gcu.model.LoginModel;

@Service
public class LoginBusinessService 
{
	public void test()
	{
		System.out.println("Testing LoginBusinessService!");
	}
	
	public List<LoginModel> getLogin()
	{
		List<LoginModel> login = new ArrayList<LoginModel>();
		login.add(new LoginModel("justinA", "1234"));
		return login;
	}
}