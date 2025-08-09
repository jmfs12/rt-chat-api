package com.jmfs.api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jmfs.api.dto.UserDTO;
import com.jmfs.api.repositories.UserRepository;
import com.jmfs.api.service.TokenService;
import com.jmfs.api.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TokenService tokenService;


    @Override
    public UserDTO getUserByToken(String token) {
        log.info("[USER SERVICE] Retrieving user by token: {}", token);
        Long userId = tokenService.extractUserId(token);
        return userRepository.findById(userId)
            .map(UserDTO::fromEntity)
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    @Override
    public List<UserDTO> getAllUsers() {
        log.info("[USER SERVICE] Retrieving all users");
        return userRepository.findAll()
            .stream()
            .map(UserDTO::fromEntity)
            .toList();
    }
    
}
