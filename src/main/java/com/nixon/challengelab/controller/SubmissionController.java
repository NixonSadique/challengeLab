package com.nixon.challengelab.controller;

import com.nixon.challengelab.dto.request.SubmissionRequest;
import com.nixon.challengelab.dto.response.SubmissionResponse;
import com.nixon.challengelab.service.SubmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
class SubmissionController {

    private final SubmissionService service;

    @PostMapping("/challenges/{challengeId}/submissions")
    ResponseEntity<SubmissionResponse> submit(@RequestBody @Valid SubmissionRequest request,
                                             @PathVariable Long challengeId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.submit(request, request.teamId(), challengeId));
    }

    @GetMapping("/challenges/{challengeId}/submissions")
    ResponseEntity<List<SubmissionResponse>> getByChallengeId(@PathVariable Long challengeId) {
        return ResponseEntity.ok(service.getByChallengeId(challengeId));
    }

    @GetMapping("/teams/{teamId}/submissions")
    ResponseEntity<List<SubmissionResponse>> getByTeamId(@PathVariable Long teamId) {
        return ResponseEntity.ok(service.getByTeamId(teamId));
    }

    @GetMapping("/submissions/me")
    ResponseEntity<List<SubmissionResponse>> getByCurrentUser() {
        return ResponseEntity.ok(service.getByCurrentUser());
    }

    @GetMapping("/submissions/{id}")
    ResponseEntity<SubmissionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @DeleteMapping("/submissions/{id}")
    ResponseEntity<Void> withdraw(@PathVariable Long id) {
        service.withdraw(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/submissions/{submissionId}/winner")
    ResponseEntity<SubmissionResponse> setWinner(@PathVariable Long submissionId) {
        return ResponseEntity.ok(service.setWinner(submissionId));
    }

}
