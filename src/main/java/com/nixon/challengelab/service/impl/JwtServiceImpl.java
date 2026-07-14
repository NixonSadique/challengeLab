package com.nixon.challengelab.service.impl;

import com.nixon.challengelab.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${application.security.jwt.name}")
    private String jwtCookieName;

    public static final String ISS = "challengeLab";
    @Value("${application.security.jwt.secret}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long expirationTime;


    @Override
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .issuedAt(new Date())
                .issuer(ISS)
                .id(UUID.randomUUID().toString())
                .signWith(getKey())
                .compact();
    }

    @Override
    public ResponseCookie generateTokenCookie(String token) {
        return ResponseCookie.from(jwtCookieName, token)
                .path("/")
                .httpOnly(true)
                .maxAge(24 * 60 * 60 * 7)
                .build();

    }

    @Override
    public String extractIdentifier(String token) {
        return extractAllClaims(token).getSubject();
    }

    @Override
    public String extractTokenFromCookie(HttpServletRequest request) {
        var jwtCookie = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(jwtCookieName))
                .findFirst().orElse(null);

        if (jwtCookie == null)
            return null;

        return jwtCookie.getValue();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        return userDetails.getUsername().equals(extractIdentifier(token)) && !isTokenExpired(token);
    }

    @Override
    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    @Override
    public ResponseCookie getCleanJwtTokenFromCookie() {
        return ResponseCookie.from(jwtCookieName, "")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                .build();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token).getPayload();
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
}
