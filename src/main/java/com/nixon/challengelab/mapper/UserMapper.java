package com.nixon.challengelab.mapper;

import com.nixon.challengelab.dto.request.RegisterRequest;
import com.nixon.challengelab.dto.response.UserResponse;
import com.nixon.challengelab.model.User;

public class UserMapper {

    public static User toUser(RegisterRequest request){
        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setUsername(request.username());
        user.setBio(request.bio());
        user.setAvatarUrl(request.avatarUrl());
        return user;
    }

    public static UserResponse toUserResponse(User user){
        return new UserResponse(user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getAvatarUrl(),
                user.getBio());
    }

}
