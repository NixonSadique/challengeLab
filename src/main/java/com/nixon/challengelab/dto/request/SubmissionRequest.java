package com.nixon.challengelab.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubmissionRequest(
        @NotBlank String title,
        @NotBlank String description,
        @NotBlank String solutionUrl
) {
}
