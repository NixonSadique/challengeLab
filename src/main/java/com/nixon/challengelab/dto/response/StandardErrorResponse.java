package com.nixon.challengelab.dto.response;

import java.time.OffsetDateTime;

public record StandardErrorResponse(
        int status,
        OffsetDateTime timestamp,
        String path,
        String title
) {
}
