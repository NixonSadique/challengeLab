package com.nixon.challengelab.mapper;

import com.nixon.challengelab.dto.request.RegisterRequest;
import com.nixon.challengelab.dto.request.UserUpdateRequest;
import com.nixon.challengelab.dto.response.UserResponse;
import com.nixon.challengelab.model.User;
import com.nixon.challengelab.model.enums.Role;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends Mapper<User, UserResponse> {

    public User toUser(RegisterRequest request){
        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setUsername(request.username());
        user.setBio(request.bio());
        user.setAvatarUrl(request.avatarUrl());
        return user;
    }

    public UserResponse toDto(User user){
        return new UserResponse(user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getAvatarUrl(),
                user.getBio());
    }

    public User update(UserUpdateRequest request, Role role, User user) {
        user.setRole(role == null ? user.getRole() : role);
        user.setBio(request.bio() == null ? user.getBio(): request.bio());
        user.setEmail(request.email() == null ? user.getEmail() : request.email());
        user.setAvatarUrl(request.avatarUrl() == null ? user.getAvatarUrl() : request.avatarUrl());
        return user;
    }

}
