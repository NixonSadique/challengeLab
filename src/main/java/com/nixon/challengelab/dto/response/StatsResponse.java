package com.nixon.challengelab.dto.response;

public record StatsResponse(
        Long totalUsers,
        Long totalChallenges,
        Long activeChallenges,
        Long totalSubmissions,
        Long totalRatings
) {
}
