package com.nixon.challengelab.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank String username,
        @NotBlank @Min(8) String password,
        @NotBlank @Email String email,
        @NotBlank String firstName,
        @NotBlank String lastName,
        String avatarUrl,
        String bio
) {
}
