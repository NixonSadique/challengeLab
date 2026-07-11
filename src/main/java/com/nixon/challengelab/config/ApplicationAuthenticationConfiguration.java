package com.nixon.challengelab.config;

import com.nixon.challengelab.exceptions.ResourceNotFoundException;
import com.nixon.challengelab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
class ApplicationAuthenticationConfiguration {

    private final UserRepository repository;

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return identifier -> repository.findByUsernameOrEmail(identifier, identifier).orElseThrow(
                () -> new ResourceNotFoundException("User not found with identifier: " + identifier)
        );
    }

}
