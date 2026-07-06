package com.nixon.challengelab.mapper;

import com.nixon.challengelab.dto.request.SubmissionRequest;
import com.nixon.challengelab.dto.response.SubmissionResponse;
import com.nixon.challengelab.model.Submission;

public class SubmissionMapper {

    public static Submission toSubmission(SubmissionRequest request) {
        Submission submission = new Submission();
        submission.setTitle(request.title());
        submission.setDescription(request.description());
        submission.setSolutionUrl(request.solutionUrl());

        return submission;
    }

    public static SubmissionResponse toSubmissionResponse(Submission submission) {
        return new SubmissionResponse(
                submission.getId(),
                submission.getTitle(),
                submission.getDescription(),
                submission.getSolutionUrl(),
                submission.getStatus()
        );
    }
}
