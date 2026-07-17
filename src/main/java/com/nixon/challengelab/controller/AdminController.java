package com.nixon.challengelab.controller;

import com.nixon.challengelab.dto.response.StatsResponse;
import com.nixon.challengelab.dto.response.UserResponse;
import com.nixon.challengelab.service.*;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
class AdminController {

    private final AdminService service;


    @PatchMapping("/challenges/close-expired")
    ResponseEntity<String> bulkClose() {
        return ResponseEntity.ok(service.closeChallenges());
    }

    @GetMapping("/users")
    ResponseEntity<Page<UserResponse>> getAll(
            @ParameterObject @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return ResponseEntity.ok(service.getAllUsers(pageable));
    }

    @GetMapping("/stats")
    ResponseEntity<StatsResponse> getStats() {
        return ResponseEntity.ok(service.getStats());
    }




}
