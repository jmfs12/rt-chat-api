package com.jmfs.api.service;

import java.util.List;

import com.jmfs.api.dto.UserDTO;

public interface UserService {
    UserDTO getUserByToken(String token);
    List<UserDTO> getAllUsers();
}
