package com.nixon.challengelab.service;

import com.nixon.challengelab.dto.request.UserUpdateRequest;
import com.nixon.challengelab.dto.response.UserResponse;
import com.nixon.challengelab.model.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserResponse getById(Long id);

    UserResponse getByEmail(String email);

    UserResponse getByUsername(String username);

    Page<UserResponse> getAll(Pageable pageable);

    UserResponse updateProfile(UserUpdateRequest request, Role role);

    UserResponse updateUser(Long id, UserUpdateRequest request, Role role);

    UserResponse myProfile();

    void deleteUser(Long id);

}
