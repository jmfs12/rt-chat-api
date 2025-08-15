package com.jmfs.api.controller;

import com.jmfs.api.config.SecurityConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jmfs.api.dto.AuthResponseDTO;
import com.jmfs.api.dto.LoginRequestDTO;
import com.jmfs.api.dto.RegisterRequestDTO;
import com.jmfs.api.service.AuthService;
import com.jmfs.api.service.TokenService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
@SecurityRequirement(name = SecurityConfig.AUTHENTICATION_SCHEME)
public class AuthController {
    public final AuthService authService;
    public final TokenService tokenService;

    @PostMapping("/login")
    @Operation(summary = "User Login", description = "Authenticate user and return JWT token")
    @ApiResponse(responseCode = "200", description = "Successful login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        AuthResponseDTO authResponseDTO = authService.login(loginRequestDTO);
        return ResponseEntity.ok(authResponseDTO);
    }

    @PostMapping("/register")
    @Operation(summary = "User Registration", description = "Register a new user and return JWT token")
    @ApiResponse(responseCode = "200", description = "Successful registration")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "404", description = "Username or email already exists")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        AuthResponseDTO authResponseDTO = authService.register(registerRequestDTO);
        return ResponseEntity.ok(authResponseDTO);
    }

    @GetMapping("/validate")
    @Operation(summary = "Validate Token", description = "Check if the provided JWT token is valid")
    @ApiResponse(responseCode = "200", description = "Token is valid")
    @ApiResponse(responseCode = "401", description = "Unauthorized, invalid token")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) {
        boolean isValid = tokenService.validateToken(token.replace("Bearer ", "").trim()) != null;
        return ResponseEntity.ok(isValid);
    }
}
