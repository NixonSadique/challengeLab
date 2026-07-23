package com.nixon.challengelab.service.impl;

import com.nixon.challengelab.dto.request.RefreshTokenRequest;
import com.nixon.challengelab.dto.response.RefreshTokenResponse;
import com.nixon.challengelab.exceptions.ForbiddenException;
import com.nixon.challengelab.exceptions.ResourceNotFoundException;
import com.nixon.challengelab.model.RefreshToken;
import com.nixon.challengelab.model.User;
import com.nixon.challengelab.repository.RefreshTokenRepository;
import com.nixon.challengelab.service.JwtService;
import com.nixon.challengelab.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository tokenRepository;
    private final JwtService jwtService;

    @Value("${application.security.refresh-token.expiration}")
    private long refreshExpiration;
    @Value("${application.security.refresh-token.name}")
    private String refreshTokenName;

    @Override
    public String createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setRevoked(false);
        refreshToken.setUser(user);
        refreshToken.setToken(Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes()));
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpiration));
        tokenRepository.save(refreshToken);

        return refreshToken.getToken();
    }


    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            tokenRepository.delete(token);
            throw new ForbiddenException("Refresh token was expired. Please make a new authentication request!");
        }
        return token;
    }


    @Override
    public RefreshTokenResponse generateNewToken(RefreshTokenRequest request) {
        RefreshToken token = tokenRepository.findByToken(request.refreshToken()).orElseThrow(
                () -> new ResourceNotFoundException("Token not found")
        );
        token = this.verifyExpiration(token);


        String jwtToken = jwtService.generateToken(token.getUser());
        return new RefreshTokenResponse(jwtToken, request.refreshToken());
    }

    @Override
    public ResponseCookie generateRefreshTokenCookie(String token) {
        return ResponseCookie.from(refreshTokenName, token)
                .path("/")
                .maxAge(refreshExpiration / 1000)
                .httpOnly(true)
                .secure(true)
                .build();
    }

    @Override
    public String getRefreshTokenFromCookies(HttpServletRequest request) {
        var cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        var refreshCookie = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(refreshTokenName))
                .findFirst().orElse(null);
        if (refreshCookie == null) {
            return null;
        }
        return refreshCookie.getValue();
    }

    @Override
    public void deleteByToken(String token) {
        tokenRepository.findByToken(token).ifPresent(tokenRepository::delete);
    }

    @Override
    public ResponseCookie getCleanRefreshTokenCookie() {
        return ResponseCookie.from(refreshTokenName, "")
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .build();
    }
}
