package com.nixon.challengelab.controller;

import com.nixon.challengelab.dto.request.RatingRequest;
import com.nixon.challengelab.dto.response.MethodArgNotValidExceptionResponse;
import com.nixon.challengelab.dto.response.RatingResponse;
import com.nixon.challengelab.dto.response.StandardErrorResponse;
import com.nixon.challengelab.service.RatingService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ratings")
@RequiredArgsConstructor
@Tag(name = "7.Ratings", description = "Submit ratings and retrieve rating details or average scores for submissions.")
@ApiResponses({
        @ApiResponse(responseCode = "401", description = "Authentication required or invalid",
                content = @Content(schema = @Schema(implementation = StandardErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Insufficient permissions",
                content = @Content(schema = @Schema(implementation = StandardErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
})
class RatingController {

    private final RatingService service;

    @Operation(summary = "Rate a submission",
            description = "Requires one of these roles: ADMIN, COMPANY, PROFESSIONAL. The authenticated user may rate a submission only once.")
    @ApiResponse(responseCode = "201", description = "Submission rated successfully",
            content = @Content(schema = @Schema(implementation = RatingResponse.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Request validation failed",
                    content = @Content(schema = @Schema(implementation = MethodArgNotValidExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Submission not found",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Submission was already rated by the current user",
                    content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    })
    @PostMapping("/submissions/{submissionId}")
    ResponseEntity<RatingResponse> rate(@PathVariable Long submissionId,
                                        @RequestBody @Valid RatingRequest request) {
        RatingResponse response = service.rate(request, submissionId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "List ratings for a submission",
            description = "Requires an authenticated user. Returns paginated ratings associated with the submission.")
    @ApiResponse(responseCode = "200", description = "Ratings retrieved successfully")
    @GetMapping("/submissions/{submissionId}")
    ResponseEntity<Page<RatingResponse>> getBySubmissionId(@PathVariable Long submissionId,
                                                           @ParameterObject @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.getBySubmissionId(submissionId, pageable));
    }

    @Operation(summary = "Get the average rating for a submission",
            description = "Requires an authenticated user who is the submission owner, a member of the submitting team, or the challenge creator.")
    @ApiResponse(responseCode = "200", description = "Average rating retrieved successfully",
            content = @Content(schema = @Schema(implementation = Double.class)))
    @ApiResponse(responseCode = "404", description = "Submission not found",
            content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    @GetMapping("/submissions/{submissionId}/average")
    ResponseEntity<Double> getAverageBySubmissionId(@PathVariable Long submissionId) {
        return ResponseEntity.ok(service.getAverageBySubmissionId(submissionId));
    }

}
