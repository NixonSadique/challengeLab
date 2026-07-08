package com.nixon.challengelab.dto.request;

public record UserUpdateRequest(
        String email,
        String avatarUrl,
        String bio
) {
}
