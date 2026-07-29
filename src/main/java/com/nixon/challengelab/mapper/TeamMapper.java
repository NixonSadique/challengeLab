package com.nixon.challengelab.mapper;

import com.nixon.challengelab.dto.request.TeamRequest;
import com.nixon.challengelab.dto.response.TeamResponse;
import com.nixon.challengelab.model.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamMapper extends Mapper<Team, TeamResponse> {

    private final TeamMemberMapper teamMemberMapper;

    public Team toTeam(TeamRequest teamRequest) {
        Team team = new Team();
        team.setName(teamRequest.name());
        return team;
    }


    public TeamResponse toDto(Team team) {
        return new TeamResponse(
                team.getId(),
                team.getName(),
                team.getChallenge().getId(),
                team.getCreatedAt(),
                teamMemberMapper.toDtoList(team.getMembers()),
                team.getSubmissions() == null ? 0 : team.getSubmissions().size()
        );
    }
}
