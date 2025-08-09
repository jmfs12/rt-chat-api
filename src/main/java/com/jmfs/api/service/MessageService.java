package com.jmfs.api.service;

import java.util.List;

import com.jmfs.api.dto.MessageDTO;

public interface MessageService {
    public MessageDTO sendMessage(MessageDTO messageDTO);
    public List<MessageDTO> getMessages(String token, Long user);
}
