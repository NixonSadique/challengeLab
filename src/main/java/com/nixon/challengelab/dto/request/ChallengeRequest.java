package com.nixon.challengelab.dto.request;

import com.nixon.challengelab.model.enums.Difficulty;
import jakarta.validation.constraints.*;

import java.time.ZonedDateTime;

public record ChallengeRequest(
        @NotBlank @Size(max = 120) String title,
        @NotBlank String description,
        @NotNull Difficulty difficulty,
        @NotBlank String category,
        Integer maxTeamSize,
        @NotNull @Future ZonedDateTime deadline
) {
}
