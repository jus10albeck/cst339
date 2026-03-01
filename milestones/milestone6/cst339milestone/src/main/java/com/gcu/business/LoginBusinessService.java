package com.gcu.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gcu.data.entity.LoginEntity;
import com.gcu.data.repository.LoginRepository;
import com.gcu.model.LoginModel;

@Service
public class LoginBusinessService 
{
	LoginRepository loginRepo;
	
	public LoginBusinessService(LoginRepository loginRepo)
	{
		this.loginRepo = loginRepo;
	}
	
	public Optional<String> authenticate(String username, String password)
	{
		return loginRepo.findByUsername(username).filter(le -> le.getPassword()
				!= null && le.getPassword().equals(password)).map(LoginEntity::getUsername);
	}
}