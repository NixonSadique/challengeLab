package com.nixon.challengelab.service;

import com.nixon.challengelab.dto.request.SubmissionRequest;
import com.nixon.challengelab.dto.response.SubmissionResponse;

import java.util.List;

public interface SubmissionService {

    SubmissionResponse submit(SubmissionRequest request, Long teamId, Long challengeId);

    List<SubmissionResponse> getByChallengeId(Long challengeId);

    List<SubmissionResponse> getByTeamId(Long teamId);

    List<SubmissionResponse> getByCurrentUser();

    void withdraw(Long id);

    SubmissionResponse getById(Long id);

    SubmissionResponse setWinner(Long submissionId);

}
