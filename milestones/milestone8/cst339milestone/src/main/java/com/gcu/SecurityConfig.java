package com.gcu;

import com.gcu.data.repository.LoginRepository;
import com.gcu.security.CryptoService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig 
{
	@Bean
	@Order(1)
	public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception 
	{
		 http.securityMatcher("/service/**")
		 	.csrf(csrf -> csrf.disable())
	     	.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .authorizeHttpRequests(auth -> auth.requestMatchers("/service/**").hasRole("SUPERVISOR"))
	        .httpBasic(Customizer.withDefaults());
		 return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception 
	{
		http
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/login/**", "/registration/**", "/css/**", "/js/**", "/images/**").permitAll()
				.anyRequest().authenticated()
					)
			.formLogin(form -> form
				.loginPage("/login/")
				.loginProcessingUrl("/login/doLogin")
				.usernameParameter("username")
				.passwordParameter("password")
				.defaultSuccessUrl("/collections/", true)
				.failureUrl("/login/?error")
			)
			.logout(logout -> logout
					.logoutUrl("/logout")
					.logoutSuccessUrl("/login/?logout")
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
	    public UserDetailsService userDetailsService(LoginRepository loginRepository, CryptoService crypto) 
	    {
	        return rawUsername -> {
	            String normalized = crypto.normalizeUsername(rawUsername);
	            byte[] h = crypto.usernameHash(normalized);

	            var login = loginRepository.findByUsernameHash(h)
	                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

	            String decryptedUsername = crypto.decrypt(login.getUsernameEnc());

	            boolean isSupervisor = "supervisor".equalsIgnoreCase(decryptedUsername);
	            String[] roles = isSupervisor ? new String[]{"SUPERVISOR"} : new String[]{"USER"};

	            return User.withUsername(decryptedUsername)
	                       .password(login.getPassword())
	                       .roles(roles)
	                       .build();
	        };
	    }

	    @Bean
	    public AuthenticationManager authenticationManager(UserDetailsService uds, PasswordEncoder encoder) 
	    {
	        var provider = new DaoAuthenticationProvider();
	        provider.setUserDetailsService(uds);
	        provider.setPasswordEncoder(encoder);
	        return new ProviderManager(provider);
	    }
	}