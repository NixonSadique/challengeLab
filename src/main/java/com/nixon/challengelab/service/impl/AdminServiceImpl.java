package com.nixon.challengelab.service.impl;

import com.nixon.challengelab.dto.response.StatsResponse;
import com.nixon.challengelab.dto.response.UserResponse;
import com.nixon.challengelab.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final ChallengeService challengeService;
    private final UserService userService;
    private final SubmissionService submissionService;
    private final RatingService ratingService;

    @Override
    public String closeChallenges() {
        return challengeService.updateToClosed();
    }

    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userService.getAll(pageable);
    }

    @Override
    public StatsResponse getStats(){
        return new StatsResponse(
                userService.countUsers(),
                challengeService.countChallenges(),
                challengeService.countActive(),
                submissionService.countSubmissions(),
                ratingService.countRatings()
        );
    }
}
