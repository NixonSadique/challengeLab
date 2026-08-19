package com.nixon.challengelab.controller;

import com.nixon.challengelab.dto.request.SubmissionRequest;
import com.nixon.challengelab.dto.response.MethodArgNotValidExceptionResponse;
import com.nixon.challengelab.dto.response.StandardErrorResponse;
import com.nixon.challengelab.dto.response.SubmissionResponse;
import com.nixon.challengelab.service.SubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "6.Submissions", description = "Submit solutions, retrieve submissions, withdraw submissions, and select winners.")
@ApiResponses({
        @ApiResponse(responseCode = "401", description = "Authentication required or invalid",
                content = @Content(schema = @Schema(implementation = StandardErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Insufficient permissions",
                content = @Content(schema = @Schema(implementation = StandardErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
})
class SubmissionController {

    private final SubmissionService service;

    @Operation(summary = "Submit a solution to a challenge",
            description = "Requires one of these roles: ADMIN, COMPANY, PROFESSIONAL, INDIVIDUAL. The challenge must be open and before its deadline. For team submissions, the authenticated user must belong to the specified team.")
    @ApiResponse(responseCode = "201", description = "Submission created successfully",
            content = @Content(schema = @Schema(implementation = SubmissionResponse.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Request validation failed",
                    content = @Content(schema = @Schema(implementation = MethodArgNotValidExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Challenge or team not found",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Challenge is closed or past its deadline",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    })
    @PostMapping("/challenges/{challengeId}/submissions")
    ResponseEntity<SubmissionResponse> submit(@RequestBody @Valid SubmissionRequest request,
                                              @PathVariable Long challengeId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.submit(request, request.teamId(), challengeId));
    }

    @Operation(summary = "List submissions for a challenge",
            description = "Requires an authenticated user. Returns submissions associated with the specified challenge.")
    @ApiResponse(responseCode = "200", description = "Submissions retrieved successfully")
    @GetMapping("/challenges/{challengeId}/submissions")
    ResponseEntity<List<SubmissionResponse>> getByChallengeId(@PathVariable Long challengeId) {
        return ResponseEntity.ok(service.getByChallengeId(challengeId));
    }

    @Operation(summary = "List submissions from a team",
            description = "Requires an authenticated user. Returns submissions associated with the specified team.")
    @ApiResponse(responseCode = "200", description = "Submissions retrieved successfully")
    @GetMapping("/teams/{teamId}/submissions")
    ResponseEntity<List<SubmissionResponse>> getByTeamId(@PathVariable Long teamId) {
        return ResponseEntity.ok(service.getByTeamId(teamId));
    }

    @Operation(summary = "List submissions from the current user",
            description = "Requires an authenticated user. Returns only submissions created by the current user.")
    @ApiResponse(responseCode = "200", description = "Submissions retrieved successfully")
    @GetMapping("/submissions/me")
    ResponseEntity<List<SubmissionResponse>> getByCurrentUser() {
        return ResponseEntity.ok(service.getByCurrentUser());
    }

    @Operation(summary = "Get a submission by ID",
            description = "Requires an authenticated user. The submission must exist.")
    @ApiResponse(responseCode = "200", description = "Submission retrieved successfully",
            content = @Content(schema = @Schema(implementation = SubmissionResponse.class)))
    @ApiResponse(responseCode = "404", description = "Submission not found",
            content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    @GetMapping("/submissions/{id}")
    ResponseEntity<SubmissionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "Withdraw a submission",
            description = "Requires an authenticated user who owns the submission or belongs to its team. An individual submission cannot be withdrawn after it becomes a winner.")
    @ApiResponse(responseCode = "204", description = "Submission withdrawn successfully")
    @ApiResponses({
            @ApiResponse(responseCode = "403", description = "User is not allowed to withdraw the submission",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Submission not found",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    })
    @DeleteMapping("/submissions/{id}")
    ResponseEntity<Void> withdraw(@PathVariable Long id) {
        service.withdraw(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Mark a submission as the winner",
            description = "Requires one of these roles: ADMIN, COMPANY, PROFESSIONAL. The challenge must be closed or past its deadline, it must not already have a winner, and the challenge creator must perform the winning action.")
    @ApiResponse(responseCode = "200", description = "Submission marked as the winner",
            content = @Content(schema = @Schema(implementation = SubmissionResponse.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Submission not found",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Challenge is ongoing or already has a winner",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    })
    @PatchMapping("/submissions/{submissionId}/winner")
    ResponseEntity<SubmissionResponse> setWinner(@PathVariable Long submissionId) {
        return ResponseEntity.ok(service.setWinner(submissionId));
    }

}
