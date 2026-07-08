package com.nixon.challengelab.mapper;

import com.nixon.challengelab.dto.request.SubmissionRequest;
import com.nixon.challengelab.dto.response.SubmissionResponse;
import com.nixon.challengelab.model.Submission;
import com.nixon.challengelab.model.enums.SubmissionStatus;
import org.springframework.stereotype.Component;

@Component
public class SubmissionMapper extends Mapper<Submission, SubmissionResponse> {

    public Submission toSubmission(SubmissionRequest request) {
        Submission submission = new Submission();
        submission.setTitle(request.title());
        submission.setDescription(request.description());
        submission.setSolutionUrl(request.solutionUrl());
        submission.setStatus(SubmissionStatus.SUBMITTED);

        return submission;
    }

    public SubmissionResponse toDto(Submission submission) {
        return new SubmissionResponse(
                submission.getId(),
                submission.getTitle(),
                submission.getDescription(),
                submission.getSolutionUrl(),
                submission.getStatus()
        );
    }
}
