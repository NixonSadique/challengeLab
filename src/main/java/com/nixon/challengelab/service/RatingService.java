package com.nixon.challengelab.service;

import com.nixon.challengelab.dto.request.RatingRequest;
import com.nixon.challengelab.dto.response.RatingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RatingService {

    RatingResponse rate(RatingRequest request, Long submissionId);

    Page<RatingResponse> getBySubmissionId(Long submissionId, Pageable pageable);

    Double getAverageBySubmissionId(Long submissionId);

    Long countRatings();

}


