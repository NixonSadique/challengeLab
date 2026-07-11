package com.nixon.challengelab.dto.response;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
