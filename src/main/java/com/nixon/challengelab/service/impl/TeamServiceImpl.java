package com.nixon.challengelab.service.impl;

import com.nixon.challengelab.dto.request.TeamRequest;
import com.nixon.challengelab.dto.response.TeamResponse;
import com.nixon.challengelab.exceptions.ConflictException;
import com.nixon.challengelab.exceptions.ResourceNotFoundException;
import com.nixon.challengelab.mapper.TeamMapper;
import com.nixon.challengelab.model.Challenge;
import com.nixon.challengelab.model.Team;
import com.nixon.challengelab.model.TeamMember;
import com.nixon.challengelab.model.User;
import com.nixon.challengelab.model.enums.ChallengeStatus;
import com.nixon.challengelab.repository.ChallengeRepository;
import com.nixon.challengelab.repository.TeamMemberRepository;
import com.nixon.challengelab.repository.TeamRepository;
import com.nixon.challengelab.service.SecurityContextService;
import com.nixon.challengelab.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.nixon.challengelab.model.enums.MemberRole.LEADER;
import static com.nixon.challengelab.model.enums.MemberRole.MEMBER;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final ChallengeRepository challengeRepository;
    private final TeamMapper teamMapper;
    private final TeamMemberRepository memberRepository;
    private final SecurityContextService contextService;

    @Override
    public TeamResponse createTeam(TeamRequest request, Long challengeId) {
        Team team = teamMapper.toTeam(request);
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(
                () -> new ResourceNotFoundException("Challenge with id: " + challengeId + "not found!")
        );

        if (!challenge.areTeamsAllowed())
            throw new ConflictException("This challenge does not allow teams!");

        if (challenge.getStatus() != ChallengeStatus.OPEN)
            throw new ConflictException("This challenge isn't open at the moment!");

        TeamMember teamMember = new TeamMember();
        teamMember.setUser(contextService.getCurrentUser());
        teamMember.setRole(LEADER);
        team.setMembers(List.of(teamMember));
        team.setChallenge(challenge);

        return teamMapper.toDto(teamRepository.save(team));
    }

    @Override
    public TeamResponse joinTeam(Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new ResourceNotFoundException("Team with id: " + teamId + "not found!")
        );
        if (memberRepository.countByTeamId(teamId) >= team.getChallenge().getMaxTeamSize())
            throw new ConflictException("The team has reached it's maximum allowed size!");

        User currentUser = contextService.getCurrentUser();
        if (memberRepository.existsByTeamIdAndUserId(teamId, currentUser.getId()))
            throw new ConflictException("The user is already part of the team!");

        TeamMember member = new TeamMember();
        member.setUser(contextService.getCurrentUser());
        member.setRole(MEMBER);
        team.getMembers().add(member);
        member.setTeam(team);


        return teamMapper.toDto(teamRepository.save(team));
    }

    @Override
    public void leaveTeam(Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new ResourceNotFoundException("Team with id: " + teamId + "not found!")
        );

        if (team.getMembers().size() == 1) {
            teamRepository.delete(team);
            return;
        }

        User currentUser = contextService.getCurrentUser();
        var member = memberRepository.findByTeamIdAndUserId(teamId, currentUser.getId()).orElseThrow(
                () -> new ResourceNotFoundException("User with id: " + currentUser.getId() + " not found on this team!")
        );

        if (member.getRole() == LEADER && team.getMembers().size() > 1) {
            team.getMembers().remove(member);
            team.getMembers().getFirst().setRole(LEADER);
        }

        team.getMembers().remove(member);
        member.setTeam(team);
        teamRepository.save(team);

    }

    @Override
    public TeamResponse getById(Long id) {
        return teamMapper.toDto(teamRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Team with id: " + id + "not found!")
        ));
    }

    @Override
    public Page<TeamResponse> getByChallengeId(Long challengeId, Pageable pageable) {
        return teamMapper.toDtoPage(teamRepository.findAllByChallengeId(challengeId, pageable));
    }
}
