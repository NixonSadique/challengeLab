package com.nixon.challengelab.controller;

import com.nixon.challengelab.dto.request.UserUpdateRequest;
import com.nixon.challengelab.dto.response.UserResponse;
import com.nixon.challengelab.model.enums.Role;
import com.nixon.challengelab.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.nixon.challengelab.model.enums.Role.ADMIN;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
class UserController {

    private final UserService service;

    @GetMapping("/{id}")
    ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/by-identifier/{username}")
    ResponseEntity<UserResponse> getByUsername(@PathVariable() String username) {
        return ResponseEntity.ok(service.getByUsername(username));
    }


    @GetMapping("/me")
    ResponseEntity<UserResponse> getProfile() {
        return ResponseEntity.ok(service.myProfile());
    }

    @PutMapping("/me")
    ResponseEntity<UserResponse> updateProfile(@RequestBody UserUpdateRequest request, Role role) {
        return ResponseEntity.ok(service.updateProfile(request, role == ADMIN ? null : role));
    }

}
