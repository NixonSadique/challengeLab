package com.nixon.challengelab.controller;

import com.nixon.challengelab.dto.response.StandardErrorResponse;
import com.nixon.challengelab.dto.response.TeamMemberResponse;
import com.nixon.challengelab.service.TeamMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/team-members")
@RequiredArgsConstructor
@Tag(name = "5.Team Members", description = "Browse team membership by team, user, username, or current user.")
@ApiResponses({
        @ApiResponse(responseCode = "401", description = "Authentication required or invalid",
                content = @Content(schema = @Schema(implementation = StandardErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Insufficient permissions",
                content = @Content(schema = @Schema(implementation = StandardErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
})
class TeamMemberController {

    private final TeamMemberService service;

    @Operation(summary = "List members of a team",
            description = "Requires an authenticated user. Returns paginated membership records for the specified team.")
    @ApiResponse(responseCode = "200", description = "Team members retrieved successfully")
    @GetMapping("/teams/{teamId}")
    ResponseEntity<Page<TeamMemberResponse>> getByTeamId(@PathVariable Long teamId,
                                                         @ParameterObject @PageableDefault() Pageable pageable) {
        return ResponseEntity.ok(service.getByTeamId(teamId, pageable));
    }

    @Operation(summary = "List teams for a user",
            description = "Requires an authenticated user. Returns paginated team memberships for the specified user.")
    @ApiResponse(responseCode = "200", description = "Team memberships retrieved successfully")
    @GetMapping("/users/{userId}")
    ResponseEntity<Page<TeamMemberResponse>> getByUserId(@PathVariable Long userId,
                                                         @ParameterObject @PageableDefault() Pageable pageable) {
        return ResponseEntity.ok(service.getByUserId(userId, pageable));
    }

    @Operation(summary = "List teams for a username",
            description = "Requires an authenticated user. Returns paginated team memberships for the specified username.")
    @ApiResponse(responseCode = "200", description = "Team memberships retrieved successfully")
    @GetMapping("/usernames/{username}")
    ResponseEntity<Page<TeamMemberResponse>> getByUsername(@PathVariable String username,
                                                           @ParameterObject @PageableDefault() Pageable pageable) {
        return ResponseEntity.ok(service.getByUsername(username, pageable));
    }

    @Operation(summary = "List teams for the current user",
            description = "Requires an authenticated user. Returns only the current user's team memberships.")
    @ApiResponse(responseCode = "200", description = "Team memberships retrieved successfully")
    @GetMapping("/me")
    ResponseEntity<Page<TeamMemberResponse>> getByCurrentUser(@ParameterObject @PageableDefault() Pageable pageable) {
        return ResponseEntity.ok(service.getByUsername(pageable));
    }

}
