package com.nixon.challengelab.service.impl;

import com.nixon.challengelab.dto.request.LoginRequest;
import com.nixon.challengelab.dto.request.RegisterRequest;
import com.nixon.challengelab.dto.response.TokenResponse;
import com.nixon.challengelab.exceptions.ConflictException;
import com.nixon.challengelab.exceptions.ResourceNotFoundException;
import com.nixon.challengelab.mapper.UserMapper;
import com.nixon.challengelab.model.User;
import com.nixon.challengelab.model.enums.Role;
import com.nixon.challengelab.repository.UserRepository;
import com.nixon.challengelab.service.AuthenticationService;
import com.nixon.challengelab.service.JwtService;
import com.nixon.challengelab.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public TokenResponse createUser(RegisterRequest registerRequest, Role role) {
        if (userRepository.existsByUsernameAndEmail(registerRequest.username(), registerRequest.email()))
            throw new ConflictException("A User with this email or username already exists!");

        User user = userMapper.toUser(registerRequest);
        user.setRole(role);
        user.setPassword(encoder.encode(registerRequest.password()));

        User savedUser = userRepository.save(user);

        var jwt = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user);

        return new TokenResponse(savedUser.getId(), savedUser.getUsername(), jwt, refreshToken);
    }

    @Override
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByUsernameOrEmail(request.identifier(), request.identifier()).orElseThrow(
                () -> new ResourceNotFoundException("Identifier not found!")
        );

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.identifier(), request.password())
        );
        return new TokenResponse(
                user.getId(),
                user.getUsername(),
                jwtService.generateToken(user),
                refreshTokenService.createRefreshToken(user));
    }
}
