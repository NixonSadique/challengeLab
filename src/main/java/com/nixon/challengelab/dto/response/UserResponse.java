package com.nixon.challengelab.dto.response;

import com.nixon.challengelab.model.enums.Role;

public record UserResponse(
        Long id,
        String username,
        String email,
        String firstName,
        String lastName,
        Role role,
        String avatarUrl,
        String bio
) {
}
