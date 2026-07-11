package com.nixon.challengelab.service.impl;

import com.nixon.challengelab.dto.request.LoginRequest;
import com.nixon.challengelab.dto.request.RegisterRequest;
import com.nixon.challengelab.dto.response.TokenResponse;
import com.nixon.challengelab.dto.response.UserResponse;
import com.nixon.challengelab.exceptions.ConflictException;
import com.nixon.challengelab.mapper.UserMapper;
import com.nixon.challengelab.model.User;
import com.nixon.challengelab.model.enums.Role;
import com.nixon.challengelab.repository.UserRepository;
import com.nixon.challengelab.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;

    @Override
    public UserResponse createUser(RegisterRequest registerRequest, Role role) {
        if (userRepository.existsByUsernameAndEmail(registerRequest.username(), registerRequest.email()))
            throw new ConflictException("A User with this email or username already exists!");

        User user = userMapper.toUser(registerRequest);
        user.setRole(role);
        user.setPassword(encoder.encode(registerRequest.password()));
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public TokenResponse login(LoginRequest request) {
        throw new RuntimeException("Not implemented Yet");
    }
}
