package com.valoexplore.backend.service;

import com.valoexplore.backend.model.User;
import com.valoexplore.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User syncUser(Jwt jwt) {
        String auth0Id = jwt.getSubject();
        String email = jwt.getClaimAsString("https://valoexplore.local/email");

        return userRepository.findByAuth0Id(auth0Id)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setAuth0Id(auth0Id);
                    newUser.setEmail(email);
                    newUser.setDisplayName(email != null ? email.split("@")[0] : "User");
                    newUser.setPlan("FREE");
                    newUser.setMaxGenerations(5);
                    newUser.setGenerationsLeft(5);
                    return userRepository.save(newUser);
                });
    }

    @Override
    @Transactional
    public User updateDisplayName(String auth0Id, String newName) {
        User user = userRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (newName != null && !newName.trim().isEmpty()) {
            user.setDisplayName(newName);
        }

        return userRepository.save(user);
    }

}