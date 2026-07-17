package com.nixon.challengelab.controller;

import com.nixon.challengelab.dto.response.TeamMemberResponse;
import com.nixon.challengelab.service.TeamMemberService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/team-members")
@RequiredArgsConstructor
class TeamMemberController {

    private final TeamMemberService service;

    @GetMapping("/teams/{teamId}")
    ResponseEntity<Page<TeamMemberResponse>> getByTeamId(@PathVariable Long teamId,
                                                        @ParameterObject @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.getByTeamId(teamId, pageable));
    }

    @GetMapping("/users/{userId}")
    ResponseEntity<Page<TeamMemberResponse>> getByUserId(@PathVariable Long userId,
                                                        @ParameterObject @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.getByUserId(userId, pageable));
    }

    @GetMapping("/usernames/{username}")
    ResponseEntity<Page<TeamMemberResponse>> getByUsername(@PathVariable String username,
                                                          @ParameterObject @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.getByUsername(username, pageable));
    }

    @GetMapping("/me")
    ResponseEntity<Page<TeamMemberResponse>> getByCurrentUser(@ParameterObject @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.getByUsername(pageable));
    }

}
