package com.jmfs.api.service;

import com.jmfs.api.dto.AuthResponseDTO;

public interface AuthService {
    public AuthResponseDTO login(String username, String password);
    public AuthResponseDTO register(String username, String password);
}
