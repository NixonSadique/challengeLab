package com.nixon.challengelab.mapper;

import com.nixon.challengelab.dto.request.ChallengeRequest;
import com.nixon.challengelab.dto.response.ChallengeResponse;
import com.nixon.challengelab.model.Challenge;

public class ChallengeMapper {

    public static Challenge toChallenge(ChallengeRequest request) {
        Challenge challenge = new Challenge();
        challenge.setTitle(request.title());
        challenge.setDescription(request.description());
        challenge.setDifficulty(request.difficulty());
        challenge.setCategory(request.category());
        challenge.setAreTeamsAllowed(request.areTeamsAllowed());
        challenge.setMaxTeamSize(request.maxTeamSize());
        challenge.setDeadline(request.deadline());
        return challenge;
    }

    public static ChallengeResponse toChallengeResponse(Challenge challenge){
        return new ChallengeResponse(
                challenge.getId(),
                challenge.getTitle(),
                challenge.getDescription(),
                challenge.getDifficulty(),
                challenge.getCategory(),
                challenge.getAreTeamsAllowed(),
                challenge.getMaxTeamSize(),
                challenge.getStatus(),
                challenge.getDeadline()
        );
    }
}
