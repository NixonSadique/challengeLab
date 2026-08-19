package com.nixon.challengelab.controller;

import com.nixon.challengelab.dto.request.LoginRequest;
import com.nixon.challengelab.dto.request.RefreshTokenRequest;
import com.nixon.challengelab.dto.request.RegisterRequest;
import com.nixon.challengelab.dto.response.MethodArgNotValidExceptionResponse;
import com.nixon.challengelab.dto.response.RefreshTokenResponse;
import com.nixon.challengelab.dto.response.StandardErrorResponse;
import com.nixon.challengelab.dto.response.TokenResponse;
import com.nixon.challengelab.model.enums.Role;
import com.nixon.challengelab.service.AuthenticationService;
import com.nixon.challengelab.service.JwtService;
import com.nixon.challengelab.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
@Tag(name = "1.Authentication", description = "User registration, login, token refresh, and logout operations.")
class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @SecurityRequirements
    @Operation(summary = "Register a new user",
            description = "Public endpoint. No authentication is required. The username and email must be unique.")
    @ApiResponse(responseCode = "201", description = "User registered successfully",
            content = @Content(schema = @Schema(implementation = TokenResponse.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Request validation failed",
                    content = @Content(schema = @Schema(implementation = MethodArgNotValidExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Username or email already exists",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    })
    @PostMapping("/register")
    ResponseEntity<TokenResponse> register(@RequestBody @Valid RegisterRequest registerRequest, @RequestParam @NotNull Role role) {
        TokenResponse tokenResponse = authenticationService
                .createUser(registerRequest, role == Role.ADMIN ? Role.INDIVIDUAL : role);

        ResponseCookie jwtCookie = jwtService.generateTokenCookie(tokenResponse.accessToken());
        ResponseCookie refreshTokenCookie = refreshTokenService.generateRefreshTokenCookie(tokenResponse.refreshToken());

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(URI.create("/api/v1/users/" + tokenResponse.userId()))
                .header(SET_COOKIE, jwtCookie.toString())
                .header(SET_COOKIE, refreshTokenCookie.toString())
                .body(tokenResponse);
    }

    @SecurityRequirements
    @Operation(summary = "Authenticate a user",
            description = "Public endpoint. No authentication is required. The identifier may be a username or email.")
    @ApiResponse(responseCode = "200", description = "User authenticated successfully",
            content = @Content(schema = @Schema(implementation = TokenResponse.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Request validation failed",
                    content = @Content(schema = @Schema(implementation = MethodArgNotValidExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "User identifier not found",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    })
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

    @SecurityRequirements
    @Operation(summary = "Refresh access and refresh tokens",
            description = "Public endpoint. No access token is required. A valid, unexpired refresh token is required.")
    @ApiResponse(responseCode = "200", description = "Tokens refreshed successfully",
            content = @Content(schema = @Schema(implementation = RefreshTokenResponse.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Refresh token not found",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Refresh token has expired",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    })
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

    @SecurityRequirements
    @Operation(summary = "Refresh tokens from the refresh cookie",
            description = "Public endpoint. No access token is required. A valid, unexpired refresh-token cookie is required.")
    @ApiResponse(responseCode = "200", description = "Tokens refreshed successfully")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Refresh token cookie not found",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Refresh token has expired",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    })
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

    @SecurityRequirements
    @Operation(summary = "Log out the current user",
            description = "Public endpoint. No authentication is required. Any existing refresh token is revoked and authentication cookies are cleared.")
    @ApiResponse(responseCode = "200", description = "User logged out successfully")
    @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
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
