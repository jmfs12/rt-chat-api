package com.jmfs.api.controller;

import java.util.List;

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
public class UserController {
    private final UserService userService;
    private final MessageService messageService;

    @GetMapping("/messages")
    public ResponseEntity<List<MessageDTO>> getMessages(@RequestHeader("Authorization") String token, @RequestHeader("User-ID") Long userId) {
        return ResponseEntity.ok(messageService.getMessages(token.replace("Bearer ", "").trim(), userId));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/me")
    public ResponseEntity<UserDTO> getCurrentUser(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(userService.getUserByToken(token.replace("Bearer ", "").trim()));
    }
}