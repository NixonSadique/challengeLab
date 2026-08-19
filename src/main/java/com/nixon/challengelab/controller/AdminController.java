package com.nixon.challengelab.controller;

import com.nixon.challengelab.dto.response.StandardErrorResponse;
import com.nixon.challengelab.dto.response.StatsResponse;
import com.nixon.challengelab.dto.response.UserResponse;
import com.nixon.challengelab.service.AdminService;
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
@Tag(name = "8.Administration", description = "Administrative user management, statistics, and expired-challenge operations.")
@ApiResponses({
        @ApiResponse(responseCode = "401", description = "Authentication required or invalid",
                content = @Content(schema = @Schema(implementation = StandardErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Administrator role required",
                content = @Content(schema = @Schema(implementation = StandardErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
})
class AdminController {

    private final AdminService service;

    @Operation(summary = "Close all expired challenges",
            description = "Requires the ADMIN role. Closes challenges whose deadlines have passed.")
    @ApiResponse(responseCode = "200", description = "Expired challenges closed successfully",
            content = @Content(schema = @Schema(implementation = String.class)))
    @PatchMapping("/challenges/close-expired")
    ResponseEntity<String> bulkClose() {
        return ResponseEntity.ok(service.closeChallenges());
    }

    @Operation(summary = "List all users",
            description = "Requires the ADMIN role. Results are paginated and sorted by ID in ascending order by default.")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    @GetMapping("/users")
    ResponseEntity<Page<UserResponse>> getAll(
            @ParameterObject @PageableDefault(sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return ResponseEntity.ok(service.getAllUsers(pageable));
    }

    @Operation(summary = "Get platform statistics",
            description = "Requires the ADMIN role. Returns aggregate counts for users, challenges, active challenges, submissions, and ratings.")
    @ApiResponse(responseCode = "200", description = "Platform statistics retrieved successfully",
            content = @Content(schema = @Schema(implementation = StatsResponse.class)))
    @GetMapping("/stats")
    ResponseEntity<StatsResponse> getStats() {
        return ResponseEntity.ok(service.getStats());
    }


}
