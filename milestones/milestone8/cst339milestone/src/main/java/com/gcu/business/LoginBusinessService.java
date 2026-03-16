package com.gcu.business;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gcu.data.repository.LoginRepository;
import com.gcu.security.CryptoService;


@Service
public class LoginBusinessService
{
    private final LoginRepository loginRepo;
    private final CryptoService crypto;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor for LoginBusinessService
     * @param loginRepo
     * @param crypto
     * @param passwordEncoder
     */
    public LoginBusinessService(LoginRepository loginRepo,
                                CryptoService crypto,
                                PasswordEncoder passwordEncoder)
    {
        this.loginRepo = loginRepo;
        this.crypto = crypto;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Authenticates a user by verifying the provided password against the stored hash and returns the decrypted username if successful.
     * @param username
     * @param password
     * @return
     */
    public Optional<String> authenticate(String username, String password)
    {
        String normalized = crypto.normalizeUsername(username);
        byte[] h = crypto.usernameHash(normalized);

        return loginRepo.findByUsernameHash(h)
                .filter(le -> le.getPassword() != null
                           && passwordEncoder.matches(password, le.getPassword()))
                .map(le -> crypto.decrypt(le.getUsernameEnc()));
    }
}
