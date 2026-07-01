package com.nixon.challengelab.dto.response;

import com.nixon.challengelab.model.enums.ChallengeStatus;
import com.nixon.challengelab.model.enums.Difficulty;

import java.time.ZonedDateTime;

public record ChallengeResponse(
        Long id,
        String title,
        String description,
        Difficulty difficulty,
        String category,
        Boolean areTeamsAllowed,
        Integer maxTeamSize,
        ChallengeStatus status,
        ZonedDateTime deadline
) {
}
