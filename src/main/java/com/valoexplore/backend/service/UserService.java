package com.valoexplore.backend.service;

import com.valoexplore.backend.model.User;
import org.springframework.security.oauth2.jwt.Jwt;

public interface UserService {
    User syncUser(Jwt jwt);
    User updateDisplayName(String auth0Id, String newName);
}
