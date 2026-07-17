package com.nixon.challengelab.controller;

import com.nixon.challengelab.dto.request.TeamRequest;
import com.nixon.challengelab.dto.response.TeamResponse;
import com.nixon.challengelab.service.TeamService;
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
@RequestMapping("/api/v1")
@RequiredArgsConstructor
class TeamController {

    private final TeamService service;

    @PostMapping("/challenges/{challengeId}/teams")
    ResponseEntity<TeamResponse> createTeam(@RequestBody @Valid TeamRequest request,
                                           @PathVariable Long challengeId) {
        TeamResponse response = service.createTeam(request, challengeId);
        return ResponseEntity.created(URI.create("api/v1/teams/" + challengeId)).body(response);
    }

    @GetMapping("/teams/{id}")
    ResponseEntity<TeamResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/challenges/{challengeId}/teams")
    ResponseEntity<Page<TeamResponse>> getByChallengeId(@PathVariable Long challengeId,
                                                       @ParameterObject @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.getByChallengeId(challengeId, pageable));
    }

    @PostMapping("/teams/{teamId}/join")
    ResponseEntity<TeamResponse> joinTeam(@PathVariable Long teamId) {
        return ResponseEntity.ok(service.joinTeam(teamId));
    }

    @DeleteMapping("/teams/{teamId}/leave")
    ResponseEntity<Void> leaveTeam(@PathVariable Long teamId) {
        service.leaveTeam(teamId);
        return ResponseEntity.noContent().build();
    }

}
