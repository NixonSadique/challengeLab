package com.nixon.challengelab.dto.response;

import com.nixon.challengelab.model.enums.SubmissionStatus;

public record SubmissionResponse(
        Long id,
        String title,
        String description,
        String solutionUrl,
        SubmissionStatus status
) {
}
