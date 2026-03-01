package com.gcu.business;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.gcu.data.entity.LoginEntity;
import com.gcu.data.entity.UserEntity;
import com.gcu.data.repository.LoginRepository;
import com.gcu.data.repository.UserRepository;
import com.gcu.model.UserRegistrationModel;

@Service
public class RegistrationBusinessService 
{

    private final LoginRepository loginRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public RegistrationBusinessService(LoginRepository loginRepo, UserRepository userRepo,
                                       PasswordEncoder passwordEncoder) 
    {
        this.loginRepo = loginRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public boolean register(UserRegistrationModel model) 
    {
        if (loginRepo.existsByUsername(model.getUsername())
                || loginRepo.existsByEmail(model.getEmail())) 
        {
            return false;
        }

        LoginEntity login = new LoginEntity();
        login.setEmail(model.getEmail());
        login.setUsername(model.getUsername());
        login.setPassword(passwordEncoder.encode(model.getPassword()));
        LoginEntity savedLogin = loginRepo.save(login);

        UserEntity user = new UserEntity();
        user.setFirstName(model.getFirstName());
        user.setLastName(model.getLastName());
        user.setLoginId(savedLogin.getId());
        userRepo.save(user);

        return true;
    }
}

