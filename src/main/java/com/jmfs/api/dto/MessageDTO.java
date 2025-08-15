package com.jmfs.api.dto;

import java.time.LocalDateTime;

import com.jmfs.api.domain.Message;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Message Data Transfer Object")
public record MessageDTO(String content, Long sender, Long receiver, LocalDateTime timestamp) {
    public static MessageDTO fromEntity(Message message) {
        return new MessageDTO(
            message.getContent(),
            message.getSender().getId(),
            message.getReceiver().getId(),
            message.getTimestamp()
        );
    }
}