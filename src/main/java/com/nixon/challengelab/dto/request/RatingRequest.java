package com.nixon.challengelab.dto.request;

import jakarta.validation.constraints.*;

public record RatingRequest(
        @NotNull @Min(1) @Max(5) Integer creativity,
        @NotNull @Min(1) @Max(5) Integer technicalQuality,
        @NotNull @Min(1) @Max(5) Integer completeness,
        @NotBlank @Size(min = 10, max = 500) String feedback
) {
}
