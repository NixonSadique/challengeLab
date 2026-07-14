package com.nixon.challengelab.controller;

import com.nixon.challengelab.dto.request.LoginRequest;
import com.nixon.challengelab.dto.request.RefreshTokenRequest;
import com.nixon.challengelab.dto.request.RegisterRequest;
import com.nixon.challengelab.dto.response.RefreshTokenResponse;
import com.nixon.challengelab.dto.response.TokenResponse;
import com.nixon.challengelab.model.enums.Role;
import com.nixon.challengelab.service.AuthenticationService;
import com.nixon.challengelab.service.JwtService;
import com.nixon.challengelab.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    ResponseEntity<TokenResponse> register(@RequestBody @Valid RegisterRequest registerRequest, @RequestParam Role role) {
        TokenResponse tokenResponse = authenticationService
                .createUser(registerRequest, role == Role.ADMIN ? Role.INDIVIDUAL : role);

        ResponseCookie jwtCookie = jwtService.generateTokenCookie(tokenResponse.accessToken());
        ResponseCookie refreshTokenCookie = refreshTokenService.generateRefreshTokenCookie(tokenResponse.refreshToken());

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(URI.create("/api/v1/user/" + tokenResponse.userId()))
                .header(SET_COOKIE, jwtCookie.toString())
                .header(SET_COOKIE, refreshTokenCookie.toString())
                .body(tokenResponse);
    }

    @PostMapping("/login")
    ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        TokenResponse tokenResponse = authenticationService.login(loginRequest);

        ResponseCookie jwtCookie = jwtService.generateTokenCookie(tokenResponse.accessToken());
        ResponseCookie refreshTokenCookie = refreshTokenService.generateRefreshTokenCookie(tokenResponse.refreshToken());

        return ResponseEntity.ok()
                .header(SET_COOKIE, jwtCookie.toString())
                .header(SET_COOKIE, refreshTokenCookie.toString())
                .body(tokenResponse);
    }

    @PostMapping("/refresh")
    ResponseEntity<RefreshTokenResponse> refresh(@RequestBody @Valid RefreshTokenRequest request) {
        RefreshTokenResponse response = refreshTokenService.generateNewToken(request);

        ResponseCookie jwtCookie = jwtService.generateTokenCookie(response.accessToken());
        ResponseCookie refreshCookie = refreshTokenService.generateRefreshTokenCookie(response.refreshToken());

        return ResponseEntity.ok()
                .header(SET_COOKIE, jwtCookie.toString())
                .header(SET_COOKIE, refreshCookie.toString())
                .body(response);
    }

    @PostMapping("/refresh-cookie")
    public ResponseEntity<?> refreshTokenCookie(HttpServletRequest request) {
        String tokenFromCookies = refreshTokenService.getRefreshTokenFromCookies(request);
        RefreshTokenResponse response = refreshTokenService.generateNewToken(new RefreshTokenRequest(tokenFromCookies));

        ResponseCookie jwtCookie = jwtService.generateTokenCookie(response.accessToken());
        ResponseCookie refreshCookie = refreshTokenService.generateRefreshTokenCookie(response.refreshToken());

        return ResponseEntity.ok()
                .header(SET_COOKIE, jwtCookie.toString())
                .header(SET_COOKIE, refreshCookie.toString())
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String tokenFromCookies = refreshTokenService.getRefreshTokenFromCookies(request);

        if (tokenFromCookies != null) {
            refreshTokenService.deleteByToken(tokenFromCookies);
        }

        ResponseCookie jwtCleanCookie = jwtService.getCleanJwtTokenFromCookie();
        ResponseCookie refreshCleanCookie = refreshTokenService.getCleanRefreshTokenCookie();

        return ResponseEntity.ok()
                .header(SET_COOKIE, jwtCleanCookie.toString())
                .header(SET_COOKIE, refreshCleanCookie.toString())
                .build();
    }

}
