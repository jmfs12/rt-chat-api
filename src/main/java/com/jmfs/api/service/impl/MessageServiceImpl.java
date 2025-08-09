package com.jmfs.api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jmfs.api.domain.User;
import com.jmfs.api.dto.MessageDTO;
import com.jmfs.api.repositories.MessageRepository;
import com.jmfs.api.repositories.UserRepository;
import com.jmfs.api.service.MessageService;
import com.jmfs.api.service.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService{
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final TokenService tokenService;

    @Override
    public MessageDTO sendMessage(MessageDTO messageDTO){
        log.info("Sending message: {}", messageDTO);
        // Logic to send the message
        // This could involve saving the message to the database, notifying users, etc.
        return messageDTO; // Return the sent message for confirmation
    }

    @Override
    public List<MessageDTO> getMessages(String token, Long user){
        Long userId = tokenService.extractUserId(token);

        User user1 = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        User user2 = userRepository.findById(user)
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + user));

        log.info("[MESSAGE SERVICE] Retrieving messages for users: {}, {}", user1.getUsername(), user2.getUsername());

        List<MessageDTO> messages = messageRepository.findByUsersId(user1.getId(), user2.getId())
            .stream()
            .map(MessageDTO::fromEntity)
            .toList();
        log.info("Retrieved {} messages for user: {}", messages.size(), user);
        return messages;
    }
}
