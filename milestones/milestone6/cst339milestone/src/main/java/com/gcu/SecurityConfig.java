package com.gcu;

import com.gcu.data.repository.LoginRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig
{

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception 
	{
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login/**", "/registration/**", "/css/**", "/js/**", "/images/**").permitAll()
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/login/")
                .loginProcessingUrl("/login/doLogin")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/collections/", true)
                .failureUrl("/login/?error"))
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login/?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() 
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(LoginRepository loginRepository) 
    {
        // Load users from your "login" table and hand to Spring Security
        return username -> loginRepository.findByUsername(username)
            .map(le -> User
                .withUsername(le.getUsername())
                .password(le.getPassword())
                .roles("USER")
                .build())
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService uds,
    		PasswordEncoder encoder) 
    {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(uds);
        provider.setPasswordEncoder(encoder);
        return new ProviderManager(provider);
    }
}
