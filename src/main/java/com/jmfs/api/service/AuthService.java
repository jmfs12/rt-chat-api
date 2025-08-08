package com.jmfs.api.service;

import com.jmfs.api.dto.AuthResponseDTO;
import com.jmfs.api.dto.LoginRequestDTO;
import com.jmfs.api.dto.RegisterRequestDTO;

public interface AuthService {
    public AuthResponseDTO login(LoginRequestDTO loginRequestDTO);
    public AuthResponseDTO register(RegisterRequestDTO registerRequestDTO);
}
