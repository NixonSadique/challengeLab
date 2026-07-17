package com.nixon.challengelab.service;

import com.nixon.challengelab.dto.response.StatsResponse;
import com.nixon.challengelab.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {

    String closeChallenges();

    Page<UserResponse> getAllUsers(Pageable pageable);

    StatsResponse getStats();
}
