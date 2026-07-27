package com.nixon.challengelab.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Size(min = 3) String username,
        @NotBlank @Size(min = 8) String password,
        @NotBlank @Email String email,
        @NotBlank String firstName,
        @NotBlank String lastName,
        String avatarUrl,
        String bio
) {
}
