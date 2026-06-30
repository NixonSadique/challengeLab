package com.nixon.challengelab.dto.request;

import com.nixon.challengelab.model.enums.ChallengeStatus;
import com.nixon.challengelab.model.enums.Difficulty;

import java.time.ZonedDateTime;

public record ChallengeFilters(
        String title,
        Difficulty difficulty,
        Boolean areTeamsAllowed,
        Integer maxAllowedTeamSize,
        ChallengeStatus status,
        ZonedDateTime deadline
) {
}
