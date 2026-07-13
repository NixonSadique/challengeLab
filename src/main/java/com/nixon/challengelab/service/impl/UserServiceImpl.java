package com.nixon.challengelab.service.impl;

import com.nixon.challengelab.dto.request.UserUpdateRequest;
import com.nixon.challengelab.dto.response.UserResponse;
import com.nixon.challengelab.exceptions.ResourceNotFoundException;
import com.nixon.challengelab.mapper.UserMapper;
import com.nixon.challengelab.model.User;
import com.nixon.challengelab.model.enums.Role;
import com.nixon.challengelab.repository.UserRepository;
import com.nixon.challengelab.service.SecurityContextService;
import com.nixon.challengelab.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    public final UserRepository userRepository;
    public final UserMapper mapper;
    public final SecurityContextService contextService;

    @Override
    public UserResponse getById(Long id) {
        return mapper.toDto(userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User with id " + id + " not found!")
        ));
    }

    @Override
    public UserResponse getByEmail(String email) {
        return mapper.toDto(userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User with email " + email + " not found!")
        ));
    }

    @Override
    public UserResponse getByUsername(String username) {
        return mapper.toDto(userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("User with username " + username + " not found!")
        ));
    }

    @Override
    public Page<UserResponse> getAll(Pageable pageable) {
        return mapper.toDtoPage(userRepository.findAll(pageable));
    }


    @Override
    public UserResponse updateProfile(UserUpdateRequest request, Role role) {
        User updated = mapper.update(request,
                role,
                contextService.getCurrentUser());
        return mapper.toDto(userRepository.save(updated));
    }

    @Override
    public UserResponse updateUser(Long id, UserUpdateRequest request, Role role) {
        User user = userRepository.findById(id).orElseThrow(
           () -> new ResourceNotFoundException("User with id " + id + " was not found!")
        );
        User updated = mapper.update(request, role, user);
        return mapper.toDto(userRepository.save(updated));
    }

    @Override
    public UserResponse myProfile() {
        return mapper.toDto(contextService.getCurrentUser());
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


}
