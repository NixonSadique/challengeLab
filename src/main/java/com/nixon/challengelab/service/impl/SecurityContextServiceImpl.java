package com.nixon.challengelab.service.impl;

import com.nixon.challengelab.exceptions.ResourceNotFoundException;
import com.nixon.challengelab.model.User;
import com.nixon.challengelab.repository.UserRepository;
import com.nixon.challengelab.service.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityContextServiceImpl implements SecurityContextService {

    private final UserRepository repository;

    public User getCurrentUser(){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() == null) {
            return null;
        }
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String identifier = userDetails.getUsername();

        return repository.findByUsernameOrEmail(identifier, identifier).orElseThrow(
                () -> new ResourceNotFoundException("User Not found.")
        );
    }

    public Long getCurrentUserId() {
        User user = getCurrentUser();
        return user != null ? user.getId() : null;
    }
}
