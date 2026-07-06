package com.nixon.challengelab.mapper;

import com.nixon.challengelab.dto.response.TeamResponse;
import com.nixon.challengelab.model.Team;

import java.util.Collections;
import java.util.List;

public class TeamMapper {

    public static TeamResponse toTeamResponse(Team team) {
        List<?> members = team.getMembers() == null
                ? Collections.emptyList()
                : team.getMembers().stream().map(TeamMemberMapper::toTeamMemberResponse).toList();

        return new TeamResponse(
                team.getId(),
                team.getName(),
                team.getChallenge().getId(),
                team.getCreatedAt(),
                team.getMembers().stream().map(TeamMemberMapper::toTeamMemberResponse).toList(),
                team.getSubmissions() == null ? 0 : team.getSubmissions().size()
        );
    }
}
