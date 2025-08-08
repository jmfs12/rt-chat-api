package com.jmfs.api.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jmfs.api.domain.User;
import com.jmfs.api.dto.AuthResponseDTO;
import com.jmfs.api.dto.LoginRequestDTO;
import com.jmfs.api.dto.RegisterRequestDTO;
import com.jmfs.api.repositories.UserRepository;
import com.jmfs.api.service.AuthService;
import com.jmfs.api.service.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponseDTO login (LoginRequestDTO loginRequestDTO) {
        log.info("[LOGIN SERVICE] Attempting to log in user: {}", loginRequestDTO.username());
        User user = userRepository.findByUsername(loginRequestDTO.username())
                .orElseThrow(() -> {
                    log.warn("[LOGIN SERVICE] User not found: {}", loginRequestDTO.username());
                    return new RuntimeException("User not found");
                });
        
        if (passwordEncoder.matches(loginRequestDTO.password(), user.getPassword())) {
            String token = tokenService.generateToken(user);
            log.info("[LOGIN SERVICE] User logged in successfully: {}", loginRequestDTO.username());
            return new AuthResponseDTO(user.getId(), user.getUsername(), token);
        } else {
            log.warn("[LOGIN SERVICE] Invalid password for user: {}", loginRequestDTO.username());
            throw new RuntimeException("Invalid credentials");
        }
    }

    @Override
    public AuthResponseDTO register (RegisterRequestDTO registerRequestDTO) {
        log.info("[REGISTER SERVICE] Attempting to register user: {}", registerRequestDTO.username());
        if (userRepository.findByUsername(registerRequestDTO.username()).isPresent()) {
            log.warn("[REGISTER SERVICE] Username already exists: {}", registerRequestDTO.username());
            throw new RuntimeException("Username already exists");
        }

        if(userRepository.findByEmail(registerRequestDTO.email()).isPresent()) {
            log.warn("[REGISTER SERVICE] Email already in use: {}", registerRequestDTO.email());
            throw new RuntimeException("Email already in use");
        }

        User newUser = new User();
        newUser.setUsername(registerRequestDTO.username());
        newUser.setPassword(passwordEncoder.encode(registerRequestDTO.password()));
        newUser.setEmail(registerRequestDTO.email());

        userRepository.save(newUser);
        String token = tokenService.generateToken(newUser);
        log.info("[REGISTER SERVICE] User registered successfully: {}", registerRequestDTO.username());
        return new AuthResponseDTO(newUser.getId(), newUser.getUsername(), token);
    }

    
}
