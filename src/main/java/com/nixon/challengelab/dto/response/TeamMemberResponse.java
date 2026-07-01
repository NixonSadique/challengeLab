package com.nixon.challengelab.dto.response;

import com.nixon.challengelab.model.enums.MemberRole;

public record TeamMemberResponse(
        Long id,
        String username,
        String email,
        String firstName,
        String lastName,
        String avatarUrl,
        MemberRole role
) {
}
