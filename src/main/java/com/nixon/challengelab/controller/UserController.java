package com.nixon.challengelab.controller;

import com.nixon.challengelab.dto.request.UserUpdateRequest;
import com.nixon.challengelab.dto.response.MethodArgNotValidExceptionResponse;
import com.nixon.challengelab.dto.response.StandardErrorResponse;
import com.nixon.challengelab.dto.response.UserResponse;
import com.nixon.challengelab.model.enums.Role;
import com.nixon.challengelab.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.nixon.challengelab.model.enums.Role.ADMIN;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "2.Users", description = "User profile retrieval and profile update operations.")
@ApiResponses({
        @ApiResponse(responseCode = "401", description = "Authentication required or invalid",
                content = @Content(schema = @Schema(implementation = StandardErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Insufficient permissions",
                content = @Content(schema = @Schema(implementation = StandardErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
})
class UserController {

    private final UserService service;

    @Operation(summary = "Get a user by ID",
            description = "Requires an authenticated user. The requested user must exist.")
    @ApiResponse(responseCode = "200", description = "User retrieved successfully",
            content = @Content(schema = @Schema(implementation = UserResponse.class)))
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    @GetMapping("/{id}")
    ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "Get a user by username",
            description = "Requires an authenticated user. The requested username must exist.")
    @ApiResponse(responseCode = "200", description = "User retrieved successfully",
            content = @Content(schema = @Schema(implementation = UserResponse.class)))
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(schema = @Schema(implementation = StandardErrorResponse.class)))
    @GetMapping("/by-identifier/{username}")
    ResponseEntity<UserResponse> getByUsername(@PathVariable() String username) {
        return ResponseEntity.ok(service.getByUsername(username));
    }


    @Operation(summary = "Get the current user profile",
            description = "Requires an authenticated user. Returns the profile associated with the current security context.")
    @ApiResponse(responseCode = "200", description = "Current user profile retrieved successfully",
            content = @Content(schema = @Schema(implementation = UserResponse.class)))
    @GetMapping("/me")
    ResponseEntity<UserResponse> getProfile() {
        return ResponseEntity.ok(service.myProfile());
    }

    @Operation(summary = "Update the current user profile",
            description = "Requires an authenticated user. Only the authenticated user's profile is updated.")
    @ApiResponse(responseCode = "200", description = "User profile updated successfully",
            content = @Content(schema = @Schema(implementation = UserResponse.class)))
    @ApiResponse(responseCode = "400", description = "Request validation failed",
            content = @Content(schema = @Schema(implementation = MethodArgNotValidExceptionResponse.class)))
    @PutMapping("/me")
    ResponseEntity<UserResponse> updateProfile(@RequestBody @Valid UserUpdateRequest request, Role role) {
        return ResponseEntity.ok(service.updateProfile(request, role == ADMIN ? null : role));
    }

}
