package com.nixon.challengelab.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SubmissionRequest(
        @NotBlank @Size(max = 120) String title,
        @NotBlank String description,
        @NotBlank String solutionUrl,
        Long teamId
) {
}
