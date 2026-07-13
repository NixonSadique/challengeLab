package com.nixon.challengelab.service.impl;

import com.nixon.challengelab.dto.response.TeamMemberResponse;
import com.nixon.challengelab.mapper.TeamMemberMapper;
import com.nixon.challengelab.repository.TeamMemberRepository;
import com.nixon.challengelab.service.SecurityContextService;
import com.nixon.challengelab.service.TeamMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamMemberServiceImpl implements TeamMemberService {

    private final TeamMemberRepository memberRepository;
    private final SecurityContextService contextService;
    private final TeamMemberMapper mapper;

    @Override
    public Page<TeamMemberResponse> getByTeamId(Long teamId, Pageable pageable) {
        return mapper.toDtoPage(memberRepository.findAllByTeamId(teamId, pageable));
    }

    @Override
    public Page<TeamMemberResponse> getByUserId(Long userId, Pageable pageable) {
        return mapper.toDtoPage(memberRepository.findAllByUserId(userId, pageable));
    }

    @Override
    public Page<TeamMemberResponse> getByUsername(String username, Pageable pageable) {
        return mapper.toDtoPage(memberRepository.findAllByUserUsername(username, pageable));
    }

    @Override
    public Page<TeamMemberResponse> getByUsername(Pageable pageable) {
        return mapper.toDtoPage(memberRepository.findAllByUserUsername(contextService.getCurrentUser().getUsername(), pageable));
    }
}
