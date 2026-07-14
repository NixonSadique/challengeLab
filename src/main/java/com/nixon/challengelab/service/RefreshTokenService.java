package com.nixon.challengelab.service;

import com.nixon.challengelab.dto.request.RefreshTokenRequest;
import com.nixon.challengelab.dto.response.RefreshTokenResponse;
import com.nixon.challengelab.model.RefreshToken;
import com.nixon.challengelab.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

public interface RefreshTokenService {

    String createRefreshToken(User user);

    RefreshToken verifyExpiration(RefreshToken token);

    RefreshTokenResponse generateNewToken(RefreshTokenRequest request);

    ResponseCookie generateRefreshTokenCookie(String token);

    String getRefreshTokenFromCookies(HttpServletRequest request);

    void deleteByToken(String token);

    ResponseCookie getCleanRefreshTokenCookie();

}
