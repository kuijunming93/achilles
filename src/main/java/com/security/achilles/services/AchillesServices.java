package com.security.achilles.services;


import com.security.achilles.model.AdminRequest;
import com.security.achilles.model.UserResponse;

public interface AchillesServices {
    boolean authenticateUser(String versionId, UserResponse response);
    boolean updateSystemVersionId(AdminRequest request, UserResponse response);
    boolean logAssetsService(UserResponse response);
}
