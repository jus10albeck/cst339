package com.gcu.business;

import org.springframework.stereotype.Service;

import com.gcu.data.entity.LoginEntity;
import com.gcu.data.entity.UserEntity;
import com.gcu.data.repository.LoginRepository;
import com.gcu.data.repository.UserRepository;
import com.gcu.model.UserRegistrationModel;

@Service
public class RegistrationBusinessService 
{
	LoginRepository loginRepo;
	UserRepository userRepo;

	public RegistrationBusinessService(LoginRepository loginRepo, UserRepository userRepo)
	{
		this.loginRepo = loginRepo;
		this.userRepo = userRepo;
	}
	
	public boolean register(UserRegistrationModel model)
	{
		// Check for duplicate username or email
		if (loginRepo.existsByUsername(model.getUsername()) ||
				loginRepo.existsByEmail(model.getEmail()))
		{
			return false;
		}
		
		// Insert into login table
		LoginEntity login = new LoginEntity();
		login.setEmail(model.getEmail());
		login.setUsername(model.getUsername());
		login.setPassword(model.getPassword());
		
		LoginEntity savedLogin = loginRepo.save(login);
		
		// Insert into user table
		UserEntity user = new UserEntity();
		user.setFirstName(model.getFirstName());
		user.setLastName(model.getLastName());
		user.setLoginId(savedLogin.getId());
			
		userRepo.save(user);
		
		return true;
	}
}
