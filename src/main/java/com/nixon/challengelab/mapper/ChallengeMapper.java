package com.nixon.challengelab.mapper;

import com.nixon.challengelab.dto.request.ChallengeRequest;
import com.nixon.challengelab.dto.response.ChallengeResponse;
import com.nixon.challengelab.model.Challenge;
import com.nixon.challengelab.model.enums.ChallengeStatus;
import org.springframework.stereotype.Component;

@Component
public class ChallengeMapper extends Mapper<Challenge, ChallengeResponse>{

    public Challenge toChallenge(ChallengeRequest request) {
        Challenge challenge = new Challenge();
        challenge.setTitle(request.title());
        challenge.setDescription(request.description());
        challenge.setDifficulty(request.difficulty());
        challenge.setCategory(request.category());
        challenge.setMaxTeamSize(request.maxTeamSize() != null ? request.maxTeamSize() : 0 );
        challenge.setDeadline(request.deadline());
        return challenge;
    }

    public ChallengeResponse toDto(Challenge challenge){
        return new ChallengeResponse(
                challenge.getId(),
                challenge.getTitle(),
                challenge.getDescription(),
                challenge.getDifficulty(),
                challenge.getCategory(),
                challenge.getMaxTeamSize(),
                challenge.getStatus(),
                challenge.getDeadline()
        );
    }

    public Challenge update(ChallengeRequest request, Challenge challenge, ChallengeStatus status) {
        challenge.setTitle(request.title());
        challenge.setDescription(request.description());
        challenge.setStatus(status);
        challenge.setDifficulty(request.difficulty());
        challenge.setCategory(request.category());
        challenge.setMaxTeamSize(request.maxTeamSize() != null ? request.maxTeamSize() : 0 );
        challenge.setDeadline(request.deadline());
        return challenge;
    }
}
