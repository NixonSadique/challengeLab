package com.nixon.challengelab.dto.request;

import com.nixon.challengelab.model.enums.MemberRole;

public record TeamMemberRequest(
        String username,
        MemberRole role
) {
}
