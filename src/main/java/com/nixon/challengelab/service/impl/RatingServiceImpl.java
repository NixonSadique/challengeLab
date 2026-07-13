package com.nixon.challengelab.service.impl;

import com.nixon.challengelab.dto.request.RatingRequest;
import com.nixon.challengelab.dto.response.RatingResponse;
import com.nixon.challengelab.exceptions.ConflictException;
import com.nixon.challengelab.exceptions.ForbiddenException;
import com.nixon.challengelab.exceptions.ResourceNotFoundException;
import com.nixon.challengelab.mapper.RatingMapper;
import com.nixon.challengelab.model.Rating;
import com.nixon.challengelab.model.Submission;
import com.nixon.challengelab.model.User;
import com.nixon.challengelab.repository.RatingRepository;
import com.nixon.challengelab.repository.SubmissionRepository;
import com.nixon.challengelab.service.RatingService;
import com.nixon.challengelab.service.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final SubmissionRepository submissionRepository;
    private final SecurityContextService contextService;
    private final RatingMapper mapper;

    @Override
    public RatingResponse rate(RatingRequest request, Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId).orElseThrow(
                () -> new ResourceNotFoundException("Submission not found!")
        );
        User currentUser = contextService.getCurrentUser();
        if (ratingRepository.existsBySubmissionIdAndUserId(submissionId, currentUser.getId()))
            throw new ConflictException("Submission already rated by the current user!");
        Rating rating = mapper.toRating(request);
        rating.setSubmission(submission);
        rating.setUser(currentUser);
        return mapper.toDto(ratingRepository.save(rating));
    }

    @Override
    public Page<RatingResponse> getBySubmissionId(Long submissionId, Pageable pageable) {
        return mapper.toDtoPage(ratingRepository.findAllBySubmissionId(submissionId, pageable));
    }

    @Override
    public Double getAverageBySubmissionId(Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId).orElseThrow(
                () -> new ResourceNotFoundException("Submission not found!")
        );

        Long userId = contextService.getCurrentUserId();
        if (!submission.getUser().getId().equals(userId) ||
                !submission.getChallenge().getCreator().getId().equals(userId)) {
            throw new ForbiddenException("Only the submitter or the challenge creator can see his average!");
        }
        return ratingRepository.getAverageRatingBySubmissionId(submissionId);
    }
}
