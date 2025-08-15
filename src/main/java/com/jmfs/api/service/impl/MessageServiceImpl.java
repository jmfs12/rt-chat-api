package com.jmfs.api.service.impl;

import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.jmfs.api.domain.Message;
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
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public MessageDTO sendMessage(MessageDTO messageDTO){
        log.info("[MESSAGE SERVICE] Sending message: {}", messageDTO);
        User sender = userRepository.findById(messageDTO.sender())
            .orElseThrow(() -> new RuntimeException("Sender not found with ID: " + messageDTO.sender()));
        User receiver = userRepository.findById(messageDTO.receiver())
            .orElseThrow(() -> new RuntimeException("Receiver not found with ID: " + messageDTO.receiver()));

        Message message = new Message(
            messageDTO,
            sender,
            receiver
        );
        
        boolean saved = messageRepository.save(message) != null;

        if (saved){
            log.info("[MESSAGE SERVICE] Message sent successfully from {} to {}", sender.getUsername(), receiver.getUsername());
            messagingTemplate.convertAndSendToUser(
                receiver.getUsername(),
                "/queue/messages",
                messageDTO
            );
        } else {
            return null;
        }

        return messageDTO;
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
        log.info("[MESSAGE SERVICE] Retrieved {} messages for user: {}", messages.size(), user);
        return messages;
    }
}
