package com.nixon.challengelab.controller;

import com.nixon.challengelab.dto.request.RatingRequest;
import com.nixon.challengelab.dto.response.RatingResponse;
import com.nixon.challengelab.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/ratings")
@RequiredArgsConstructor
class RatingController {

    private final RatingService service;

    @PostMapping("/submissions/{submissionId}")
    ResponseEntity<RatingResponse> rate(@PathVariable Long submissionId,
                                        @RequestBody @Valid RatingRequest request) {
        RatingResponse response = service.rate(request, submissionId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/submissions/{submissionId}")
    ResponseEntity<Page<RatingResponse>> getBySubmissionId(@PathVariable Long submissionId,
                                                           @ParameterObject @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.getBySubmissionId(submissionId, pageable));
    }

    @GetMapping("/submissions/{submissionId}/average")
    ResponseEntity<Double> getAverageBySubmissionId(@PathVariable Long submissionId) {
        return ResponseEntity.ok(service.getAverageBySubmissionId(submissionId));
    }

}
