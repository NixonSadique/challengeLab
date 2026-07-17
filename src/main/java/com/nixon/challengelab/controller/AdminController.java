package com.nixon.challengelab.controller;

import com.nixon.challengelab.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
class AdminController {

    private final ChallengeService challengeService;

    @PatchMapping("/challenges/close-expired")
    ResponseEntity<String> bulkClose() {
        return ResponseEntity.ok(challengeService.updateToClosed());
    }


}
