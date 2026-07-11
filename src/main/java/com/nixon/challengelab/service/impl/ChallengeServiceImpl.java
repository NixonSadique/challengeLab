package com.nixon.challengelab.service.impl;

import com.nixon.challengelab.dto.request.ChallengeFilters;
import com.nixon.challengelab.dto.request.ChallengeRequest;
import com.nixon.challengelab.dto.response.ChallengeResponse;
import com.nixon.challengelab.exceptions.ResourceNotFoundException;
import com.nixon.challengelab.mapper.ChallengeMapper;
import com.nixon.challengelab.model.Challenge;
import com.nixon.challengelab.model.enums.ChallengeStatus;
import com.nixon.challengelab.repository.ChallengeRepository;
import com.nixon.challengelab.security.SecurityContextService;
import com.nixon.challengelab.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.nixon.challengelab.repository.ChallengeRepository.withFilters;

@Service
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService {

    private final ChallengeRepository repository;
    private final ChallengeMapper mapper;
    private final SecurityContextService contextService;

    @Override
    public ChallengeResponse createChallenge(ChallengeRequest challengeRequest) {
        Challenge challenge = mapper.toChallenge(challengeRequest);
        challenge.setCreator(contextService.getCurrentUser());

        return mapper.toDto(repository.save(challenge));
    }

    @Override
    public List<ChallengeResponse> getByCreator(Long id) {
        return mapper.toDtoList(repository.findAllByCreatorId(id));
    }

    @Override
    public Page<ChallengeResponse> getByCreator(String email, String username, Pageable pageable) {
        return mapper.toDtoPage(repository.findAllByCreatorEmailOrCreatorUsername(email, username, pageable));
    }

    @Override
    public String updateToClosed() {
        var lines = repository.updateChallengeStatusToClosed(ChallengeStatus.CLOSED);
        return lines + " Challenges were closed!";
    }


    @Override
    public Page<ChallengeResponse> getWithFilters(ChallengeFilters filters, Pageable pageable) {
        return mapper.toDtoPage(repository.findAll(withFilters(filters), pageable));
    }

    @Override
    public ChallengeResponse getById(Long id) {
        return mapper.toDto(repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Challenge not found with id: " + id)
        ));
    }

    @Override
    public ChallengeResponse updateChallenge(Long id, ChallengeRequest request, ChallengeStatus status) {
        Challenge challenge = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Challenge not found with id: " + id)
        );

        Challenge updated = mapper.update(request, challenge, status);

        return mapper.toDto(repository.save(updated));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
