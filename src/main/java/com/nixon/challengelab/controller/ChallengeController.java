package com.nixon.challengelab.controller;

import com.nixon.challengelab.dto.request.ChallengeFilters;
import com.nixon.challengelab.dto.request.ChallengeRequest;
import com.nixon.challengelab.dto.request.UpdateChallengeStatusRequest;
import com.nixon.challengelab.dto.response.ChallengeResponse;
import com.nixon.challengelab.service.ChallengeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
class ChallengeController {

    private final ChallengeService service;

    @PostMapping("/challenges")
    ResponseEntity<ChallengeResponse> createChallenge(@RequestBody @Valid ChallengeRequest request) {
        ChallengeResponse response = service.createChallenge(request);
        return ResponseEntity.created(URI.create("/api/v1/challenges/" + response.id())).body(response);
    }

    @GetMapping("/challenges")
    ResponseEntity<Page<ChallengeResponse>> getWithFilters(@ParameterObject ChallengeFilters filters,
                                                           @ParameterObject @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.getWithFilters(filters, pageable));
    }

    @GetMapping("/challenges/{id}")
    ResponseEntity<ChallengeResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/users/{id}/challenges")
    ResponseEntity<List<ChallengeResponse>> getByCreator(@PathVariable Long id){
        return ResponseEntity.ok(service.getByCreator(id));
    }

    @GetMapping("/users/by-identifier/{identifier}/challenges")
    ResponseEntity<Page<ChallengeResponse>> getByCreator(@PathVariable String identifier,
                                                         @ParameterObject @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.getByCreator(identifier, identifier,pageable));
    }

    @GetMapping("/challenges/me")
    ResponseEntity<Page<ChallengeResponse>> getMyChallenges(@ParameterObject @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.getFromCurrentCreator(pageable));
    }

    @PutMapping("/challenges/{id}")
    ResponseEntity<ChallengeResponse> updateChallenge(@PathVariable Long id,
                                                      @RequestBody @Valid ChallengeRequest request) {
        return ResponseEntity.ok(service.updateChallenge(id, request));
    }

    @PatchMapping("/challenges/{id}/status")
    ResponseEntity<ChallengeResponse> updateStatus(@PathVariable Long id,
                                           @RequestBody @Valid UpdateChallengeStatusRequest request) {
        return ResponseEntity.ok(service.updateChallenge(id, request.status()));
    }

    @DeleteMapping("/challenges/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
