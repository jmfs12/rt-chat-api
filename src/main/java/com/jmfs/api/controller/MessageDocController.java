package com.jmfs.api.controller;

import com.jmfs.api.config.SecurityConfig;
import com.jmfs.api.dto.MessageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ws")
@Tag(name  = "Messages", description = "WebSocket message endpoints")
@SecurityRequirement(name = SecurityConfig.AUTHENTICATION_SCHEME)
public class MessageDocController {

    @Operation(summary = "Send a message via WebSocket",
                description = "Send a message to /app/send-message over STOMP")
    @ApiResponse(responseCode = "201", description = "Message sent succesfully")
    @PostMapping("/send-message")
    public ResponseEntity<MessageDTO> sendMessage(@RequestBody MessageDTO messageDTO) {
        return ResponseEntity.ok(messageDTO);
    }
}
