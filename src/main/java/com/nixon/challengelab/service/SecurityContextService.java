package com.nixon.challengelab.service;

import com.nixon.challengelab.model.User;

public interface SecurityContextService {

    User getCurrentUser();

    Long getCurrentUserId();
}
