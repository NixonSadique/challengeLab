package com.nixon.challengelab.dto.request;

import com.nixon.challengelab.model.enums.Difficulty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;

public record ChallengeRequest(
        @NotBlank @Max(120) String title,
        @NotBlank String description,
        @NotBlank Difficulty difficulty,
        @NotBlank String category,
        Integer maxTeamSize,
        @NotNull @Future ZonedDateTime deadline
) {
}
