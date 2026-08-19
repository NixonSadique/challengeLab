package com.nixon.challengelab.controller;

import com.nixon.challengelab.dto.request.TeamRequest;
import com.nixon.challengelab.dto.response.MethodArgNotValidExceptionResponse;
import com.nixon.challengelab.dto.response.StandardErrorResponse;
import com.nixon.challengelab.dto.response.TeamResponse;
import com.nixon.challengelab.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "4.Teams", description = "Create and manage challenge teams, including joining and leaving teams.")
@ApiResponses({
        @ApiResponse(responseCode = "401", description = "Authentication required or invalid",
                content = @Content(schema = @Schema(implementation = StandardErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Insufficient permissions",
                content = @Content(schema = @Schema(implementation = StandardErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
})
class TeamController {

    private final TeamService service;

    @Operation(summary = "Create a team for a challenge",
            description = "Requires one of these roles: ADMIN, COMPANY, PROFESSIONAL, INDIVIDUAL. The challenge must allow teams and still be open. The authenticated user becomes the team leader.")
    @ApiResponse(responseCode = "201", description = "Team created successfully",
            content = @Content(schema = @Schema(implementation = TeamResponse.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Request validation failed",
                    content = @Content(schema = @Schema(implementation = MethodArgNotValidExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Challenge not found",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Teams are disabled or the challenge is closed",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    })
    @PostMapping("/challenges/{challengeId}/teams")
    ResponseEntity<TeamResponse> createTeam(@RequestBody @Valid TeamRequest request,
                                            @PathVariable Long challengeId) {
        TeamResponse response = service.createTeam(request, challengeId);
        return ResponseEntity.created(URI.create("api/v1/teams/" + response.id())).body(response);
    }

    @Operation(summary = "Get a team by ID",
            description = "Requires an authenticated user. The team must exist.")
    @ApiResponse(responseCode = "200", description = "Team retrieved successfully",
            content = @Content(schema = @Schema(implementation = TeamResponse.class)))
    @ApiResponse(responseCode = "404", description = "Team not found",
            content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    @GetMapping("/teams/{id}")
    ResponseEntity<TeamResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "List teams for a challenge",
            description = "Requires an authenticated user. Returns the teams associated with the challenge.")
    @ApiResponse(responseCode = "200", description = "Teams retrieved successfully")
    @GetMapping("/challenges/{challengeId}/teams")
    ResponseEntity<Page<TeamResponse>> getByChallengeId(@PathVariable Long challengeId,
                                                        @ParameterObject @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.getByChallengeId(challengeId, pageable));
    }

    @Operation(summary = "Join a team",
            description = "Requires one of these roles: ADMIN, COMPANY, PROFESSIONAL, INDIVIDUAL. The team must have available capacity, and the authenticated user must not already be a member.")
    @ApiResponse(responseCode = "200", description = "User joined the team successfully",
            content = @Content(schema = @Schema(implementation = TeamResponse.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Team not found",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Team is full or user is already a member",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    })
    @PostMapping("/teams/{teamId}/join")
    ResponseEntity<TeamResponse> joinTeam(@PathVariable Long teamId) {
        return ResponseEntity.ok(service.joinTeam(teamId));
    }

    @Operation(summary = "Leave a team",
            description = "Requires an authenticated user who is a member of the team. If the departing member is the leader, leadership is transferred to another member.")
    @ApiResponse(responseCode = "204", description = "User left the team successfully")
    @ApiResponse(responseCode = "404", description = "Team or membership not found",
            content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    @DeleteMapping("/teams/{teamId}/leave")
    ResponseEntity<Void> leaveTeam(@PathVariable Long teamId) {
        service.leaveTeam(teamId);
        return ResponseEntity.noContent().build();
    }

}
