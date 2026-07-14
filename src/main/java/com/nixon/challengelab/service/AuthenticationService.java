package com.nixon.challengelab.service;

import com.nixon.challengelab.dto.request.LoginRequest;
import com.nixon.challengelab.dto.request.RegisterRequest;
import com.nixon.challengelab.dto.response.TokenResponse;
import com.nixon.challengelab.model.enums.Role;

public interface AuthenticationService {

    TokenResponse createUser(RegisterRequest registerRequest, Role role);

    TokenResponse login(LoginRequest request);


}
