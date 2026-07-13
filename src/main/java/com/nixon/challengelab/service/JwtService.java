package com.nixon.challengelab.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    String generateToken(UserDetails userDetails);

    ResponseCookie generateTokenCookie(String token);

    String extractIdentifier(String token);

    String extractTokenFromCookie(HttpServletRequest request);

    boolean isTokenValid(String token, UserDetails userDetails);

    boolean isTokenExpired(String token);



}
