package com.gcu.business;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.gcu.data.entity.LoginEntity;
import com.gcu.data.entity.UserEntity;
import com.gcu.data.repository.LoginRepository;
import com.gcu.data.repository.UserRepository;
import com.gcu.model.UserRegistrationModel;
import com.gcu.security.CryptoService;


@Service
public class RegistrationBusinessService
{
    private final LoginRepository loginRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final CryptoService crypto;

    /** 
     * Constructor
     * @param loginRepo
     * @param userRepo
     * @param passwordEncoder
     * @param crypto
     */
    public RegistrationBusinessService(LoginRepository loginRepo, UserRepository userRepo,
                                       PasswordEncoder passwordEncoder, CryptoService crypto) 
    {
        this.loginRepo = loginRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.crypto = crypto;
    }

    /**
     * Registers a new user by validating uniqueness, encrypting credentials,
     *  hashing the username, and persisting the login and user records in a single transaction
     * @param model
     * @return
     */
    @Transactional
    public boolean register(UserRegistrationModel model)
    {
        String normalized = crypto.normalizeUsername(model.getUsername());
        byte[] h = crypto.usernameHash(normalized);

        if (loginRepo.existsByUsernameHash(h) || loginRepo.existsByEmail(model.getEmail())) 
            return false;

        LoginEntity login = new LoginEntity();
        login.setEmail(model.getEmail());
        login.setUsernameEnc(crypto.encrypt(model.getUsername()));
        login.setUsernameHash(h);
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