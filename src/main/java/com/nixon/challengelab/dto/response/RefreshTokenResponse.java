package com.nixon.challengelab.dto.response;

public record RefreshTokenResponse(
        String accessToken,
        String refreshToken
) {
}
