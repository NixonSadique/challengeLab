package com.nixon.challengelab.mapper;

import com.nixon.challengelab.dto.response.TeamMemberResponse;
import com.nixon.challengelab.model.TeamMember;
import com.nixon.challengelab.model.User;
import org.springframework.stereotype.Component;

@Component
public class TeamMemberMapper extends  Mapper<TeamMember, TeamMemberResponse> {


    public TeamMemberResponse toDto(TeamMember teamMember) {
        User user = teamMember.getUser();

        return new TeamMemberResponse(
                teamMember.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getAvatarUrl(),
                teamMember.getRole()
        );
    }
}
