package com.nixon.challengelab.service;

import com.nixon.challengelab.dto.request.ChallengeFilters;
import com.nixon.challengelab.dto.request.ChallengeRequest;
import com.nixon.challengelab.dto.response.ChallengeResponse;
import com.nixon.challengelab.model.enums.ChallengeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChallengeService {

    ChallengeResponse createChallenge(ChallengeRequest challengeRequest);

    List<ChallengeResponse> getByCreator(Long id);

    Page<ChallengeResponse> getByCreator(String email, String username, Pageable pageable);

    String updateToClosed();


    Page<ChallengeResponse> getWithFilters(ChallengeFilters filters, Pageable pageable);

    ChallengeResponse getById(Long id);

    ChallengeResponse updateChallenge(Long id, ChallengeRequest request, ChallengeStatus status);

    void deleteById(Long id);

}
