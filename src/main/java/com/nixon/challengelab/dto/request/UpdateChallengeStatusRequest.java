package com.nixon.challengelab.dto.request;

import com.nixon.challengelab.model.enums.ChallengeStatus;

public record UpdateChallengeStatusRequest(
        ChallengeStatus status
) {
}
