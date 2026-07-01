package com.nixon.challengelab.dto.response;

import java.time.Instant;
import java.util.List;

public record TeamResponse(
        Long id,
        String name,
        Long challengeId,
        Instant createdAt,
        List<TeamMemberResponse> members,
        Integer submissionCount
) {
}
