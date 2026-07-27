package com.nixon.challengelab.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank @Size(min = 3) String identifier,
        @NotBlank @Size(min = 3) String password
) {
}
