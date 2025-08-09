package com.jmfs.api.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.jmfs.api.dto.MessageDTO;
import com.jmfs.api.service.MessageService;

@Controller
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("/send-message")
    public MessageDTO sendMessage(MessageDTO message) {
        return messageService.sendMessage(message);
    }
}
