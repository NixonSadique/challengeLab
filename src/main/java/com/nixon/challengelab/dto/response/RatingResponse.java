package com.nixon.challengelab.dto.response;

public record RatingResponse(
        Long id,
        Integer creativity,
        Integer technicalQuality,
        Integer completeness,
        String feedback,
        UserResponse ratedBy,
        Long submissionId
) {
}
