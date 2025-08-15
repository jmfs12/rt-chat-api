package com.jmfs.api.controller;

import java.util.List;

import com.jmfs.api.config.SecurityConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jmfs.api.dto.MessageDTO;
import com.jmfs.api.dto.UserDTO;
import com.jmfs.api.service.MessageService;
import com.jmfs.api.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "User", description = "Endpoints for user management and message retrieval")
@SecurityRequirement(name = SecurityConfig.AUTHENTICATION_SCHEME)
public class UserController {
    private final UserService userService;
    private final MessageService messageService;

    @GetMapping("/messages")
    @Operation(summary = "Get Messages", description = "Retrieve messages for the authenticated user")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of messages")
    @ApiResponse(responseCode = "401", description = "Unauthorized, invalid token")
    @ApiResponse(responseCode = "404", description = "No messages found for the user")
    public ResponseEntity<List<MessageDTO>> getMessages(@RequestHeader("Authorization") String token, @RequestHeader("User-ID") Long userId) {
        return ResponseEntity.ok(messageService.getMessages(token.replace("Bearer ", "").trim(), userId));
    }

    @Operation(summary = "Get All Users", description = "Retrieve a list of all registered users")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of users")
    @ApiResponse(responseCode = "404", description = "No users found")
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/me")
    @Operation(summary = "Get Current User", description = "Retrieve the details of the currently authenticated user")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of current user")
    @ApiResponse(responseCode = "401", description = "Unauthorized, invalid token")
    public ResponseEntity<UserDTO> getCurrentUser(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(userService.getUserByToken(token.replace("Bearer ", "").trim()));
    }
}