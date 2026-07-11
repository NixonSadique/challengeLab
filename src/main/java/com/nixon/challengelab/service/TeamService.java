package com.nixon.challengelab.service;

import com.nixon.challengelab.dto.request.TeamRequest;
import com.nixon.challengelab.dto.response.TeamResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeamService {
    TeamResponse createTeam(TeamRequest request, Long challengeId);

    TeamResponse joinTeam(Long teamId);

    void leaveTeam(Long teamId);

    TeamResponse getById(Long id);

    Page<TeamResponse> getByChallengeId(Long challengeId, Pageable pageable);

}
