package com.nixon.challengelab.controller;

import com.nixon.challengelab.dto.request.ChallengeFilters;
import com.nixon.challengelab.dto.request.ChallengeRequest;
import com.nixon.challengelab.dto.request.UpdateChallengeStatusRequest;
import com.nixon.challengelab.dto.response.ChallengeResponse;
import com.nixon.challengelab.dto.response.MethodArgNotValidExceptionResponse;
import com.nixon.challengelab.dto.response.StandardErrorResponse;
import com.nixon.challengelab.service.ChallengeService;
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
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "3.Challenges", description = "Create, browse, filter, update, and delete coding challenges.")
@ApiResponses({
        @ApiResponse(responseCode = "401", description = "Authentication required or invalid",
                content = @Content(schema = @Schema(implementation = StandardErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Insufficient permissions",
                content = @Content(schema = @Schema(implementation = StandardErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
})
class ChallengeController {

    private final ChallengeService service;

    @Operation(summary = "Create a challenge",
            description = "Requires one of these roles: ADMIN, COMPANY, PROFESSIONAL. The authenticated user becomes the challenge creator.")
    @ApiResponse(responseCode = "201", description = "Challenge created successfully",
            content = @Content(schema = @Schema(implementation = ChallengeResponse.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Request validation failed",
                    content = @Content(schema = @Schema(implementation = MethodArgNotValidExceptionResponse.class)))
    })
    @PostMapping("/challenges")
    ResponseEntity<ChallengeResponse> createChallenge(@RequestBody @Valid ChallengeRequest request) {
        ChallengeResponse response = service.createChallenge(request);
        return ResponseEntity.created(URI.create("/api/v1/challenges/" + response.id())).body(response);
    }

    @Operation(summary = "Search and filter challenges",
            description = "Requires an authenticated user. Filters are optional and may include title, difficulty, status, deadline, and maximum team size.")
    @ApiResponse(responseCode = "200", description = "Challenges retrieved successfully")
    @ApiResponse(responseCode = "400", description = "Invalid filter or pagination parameters",
            content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    @GetMapping("/challenges")
    ResponseEntity<Page<ChallengeResponse>> getWithFilters(@ParameterObject ChallengeFilters filters,
                                                           @ParameterObject @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.getWithFilters(filters, pageable));
    }

    @Operation(summary = "Get a challenge by ID",
            description = "Requires an authenticated user. The challenge must exist.")
    @ApiResponse(responseCode = "200", description = "Challenge retrieved successfully",
            content = @Content(schema = @Schema(implementation = ChallengeResponse.class)))
    @ApiResponse(responseCode = "404", description = "Challenge not found",
            content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    @GetMapping("/challenges/{id}")
    ResponseEntity<ChallengeResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "Get challenges created by a user",
            description = "Requires an authenticated user. Returns challenges associated with the specified creator; an unknown creator may return an empty result.")
    @ApiResponse(responseCode = "200", description = "Challenges retrieved successfully")
    @GetMapping("/users/{id}/challenges")
    ResponseEntity<List<ChallengeResponse>> getByCreator(@PathVariable Long id) {
        return ResponseEntity.ok(service.getByCreator(id));
    }

    @Operation(summary = "Get challenges created by a user identifier",
            description = "Requires an authenticated user. The identifier is matched against the creator's email or username.")
    @ApiResponse(responseCode = "200", description = "Challenges retrieved successfully")
    @GetMapping("/users/by-identifier/{identifier}/challenges")
    ResponseEntity<Page<ChallengeResponse>> getByCreator(@PathVariable String identifier,
                                                         @ParameterObject @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.getByCreator(identifier, identifier, pageable));
    }

    @Operation(summary = "Get challenges created by the current user",
            description = "Requires an authenticated user. Returns only challenges created by the current user.")
    @ApiResponse(responseCode = "200", description = "Challenges retrieved successfully")
    @GetMapping("/challenges/me")
    ResponseEntity<Page<ChallengeResponse>> getMyChallenges(@ParameterObject @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.getFromCurrentCreator(pageable));
    }

    @Operation(summary = "Update a challenge",
            description = "Requires an authenticated user. The challenge must exist. The caller must be the challenge creator.")
    @ApiResponse(responseCode = "200", description = "Challenge updated successfully",
            content = @Content(schema = @Schema(implementation = ChallengeResponse.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Request validation failed",
                    content = @Content(schema = @Schema(implementation = MethodArgNotValidExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Challenge not found",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    })
    @PutMapping("/challenges/{id}")
    ResponseEntity<ChallengeResponse> updateChallenge(@PathVariable Long id,
                                                      @RequestBody @Valid ChallengeRequest request) {
        return ResponseEntity.ok(service.updateChallenge(id, request));
    }

    @Operation(summary = "Update a challenge status",
            description = "Requires an authenticated user. The challenge must exist. Closing a challenge also sets its deadline to the current time.")
    @ApiResponse(responseCode = "200", description = "Challenge status updated successfully",
            content = @Content(schema = @Schema(implementation = ChallengeResponse.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Request validation failed",
                    content = @Content(schema = @Schema(implementation = MethodArgNotValidExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Challenge not found",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    })
    @PatchMapping("/challenges/{id}/status")
    ResponseEntity<ChallengeResponse> updateStatus(@PathVariable Long id,
                                                   @RequestBody @Valid UpdateChallengeStatusRequest request) {
        return ResponseEntity.ok(service.updateChallenge(id, request.status()));
    }

    @Operation(summary = "Delete a challenge",
            description = "Requires an authenticated user. The caller must be the challenge creator.")
    @ApiResponse(responseCode = "204", description = "Challenge deleted successfully")
    @ApiResponse(responseCode = "404", description = "Challenge not found",
            content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    @DeleteMapping("/challenges/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
