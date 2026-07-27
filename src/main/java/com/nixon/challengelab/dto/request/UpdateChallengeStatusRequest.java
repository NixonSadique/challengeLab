package com.nixon.challengelab.dto.request;

import com.nixon.challengelab.model.enums.ChallengeStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateChallengeStatusRequest(
        @NotNull ChallengeStatus status
) {
}
