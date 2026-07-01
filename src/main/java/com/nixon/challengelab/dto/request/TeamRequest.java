package com.nixon.challengelab.dto.request;

import java.util.List;

public record TeamRequest(
        Long challengeId,
        List<TeamMemberRequest> members
) {
}
