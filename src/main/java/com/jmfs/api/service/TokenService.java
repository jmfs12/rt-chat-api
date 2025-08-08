package com.jmfs.api.service;

import com.jmfs.api.domain.User;

public interface TokenService {
    public String generateToken(User user);
    public String validateToken(String token);
    public Long extractUserId(String token);
}
