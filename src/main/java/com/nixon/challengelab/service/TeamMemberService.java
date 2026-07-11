package com.nixon.challengelab.service;

import com.nixon.challengelab.dto.response.TeamMemberResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeamMemberService {

    Page<TeamMemberResponse> getByTeamId(Long teamId, Pageable pageable);

    Page<TeamMemberResponse> getByUserId(Long userId, Pageable pageable);

    Page<TeamMemberResponse> getByUsername(String username,Pageable pageable);

    Page<TeamMemberResponse> getByUsername(Pageable pageable);

}
