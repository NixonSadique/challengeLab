package com.nixon.challengelab.service.impl;

import com.nixon.challengelab.dto.request.SubmissionRequest;
import com.nixon.challengelab.dto.response.SubmissionResponse;
import com.nixon.challengelab.exceptions.ConflictException;
import com.nixon.challengelab.exceptions.ForbiddenException;
import com.nixon.challengelab.exceptions.ResourceNotFoundException;
import com.nixon.challengelab.mapper.SubmissionMapper;
import com.nixon.challengelab.model.Submission;
import com.nixon.challengelab.model.Team;
import com.nixon.challengelab.model.User;
import com.nixon.challengelab.model.enums.ChallengeStatus;
import com.nixon.challengelab.model.enums.SubmissionStatus;
import com.nixon.challengelab.repository.ChallengeRepository;
import com.nixon.challengelab.repository.SubmissionRepository;
import com.nixon.challengelab.repository.TeamRepository;
import com.nixon.challengelab.service.SecurityContextService;
import com.nixon.challengelab.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final ChallengeRepository challengeRepository;
    private final TeamRepository teamRepository;
    private final SubmissionMapper mapper;
    private final SecurityContextService contextService;

    @Override
    public SubmissionResponse submit(SubmissionRequest request, Long teamId, Long challengeId) {
        Submission submission = mapper.toSubmission(request);
        var challenge = challengeRepository.findById(challengeId).orElseThrow(
                () -> new ResourceNotFoundException("Challenge with id: " + challengeId + " not found1")
        );

        if (challenge.getStatus() != ChallengeStatus.OPEN || challenge.getDeadline().isBefore(ZonedDateTime.now())) {
            throw new ConflictException("Challenge not open or past deadline!");
        }
        submission.setChallenge(challenge);

        User currentUser = contextService.getCurrentUser();
        if (teamId == null) {
            submission.setUser(currentUser);

            return mapper.toDto(submissionRepository.save(submission));
        }

        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new ResourceNotFoundException("Team with id: " + teamId + " not found!")
        );

        if (team.getMembers().stream().anyMatch(teamMember -> teamMember.getUser().equals(currentUser))) {
            throw new ForbiddenException("User not part of the requested team!");
        }

        submission.setTeam(team);

        return mapper.toDto(submissionRepository.save(submission));
    }

    @Override
    public List<SubmissionResponse> getByChallengeId(Long challengeId) {
        return mapper.toDtoList(submissionRepository.findAllByChallengeId(challengeId));
    }

    @Override
    public List<SubmissionResponse> getByTeamId(Long teamId) {
        return mapper.toDtoList(submissionRepository.findAllByTeamId(teamId));
    }

    @Override
    public List<SubmissionResponse> getByCurrentUser() {
        return mapper.toDtoList(submissionRepository.findAllByUserId(contextService.getCurrentUserId()));
    }

    @Override
    public void withdraw(Long id) {
        Submission submission = submissionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Submission Not found!")
        );

        if (submission.getUser().getId().equals(contextService.getCurrentUserId())
                && submission.getStatus() != SubmissionStatus.WINNER) {
            submissionRepository.delete(submission);
        }
    }

    @Override
    public SubmissionResponse getById(Long id) {
        return mapper.toDto(submissionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Submission Not found!")
        ));
    }

    @Override
    public SubmissionResponse setWinner(Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId).orElseThrow(
                () -> new ResourceNotFoundException("Submission Not found!")
        );

        if (submission.getChallenge().getCreator().getId().equals(contextService.getCurrentUserId()))
            submission.setStatus(SubmissionStatus.WINNER);


        return mapper.toDto(submissionRepository.save(submission));
    }
}
