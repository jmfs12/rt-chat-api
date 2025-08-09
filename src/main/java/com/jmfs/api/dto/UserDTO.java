package com.jmfs.api.dto;

import com.jmfs.api.domain.User;

public record UserDTO(Long id, String username, String email) {
    public static UserDTO fromEntity(User user) {
        return new UserDTO(
            user.getId(),
            user.getUsername(),
            user.getEmail()
        );
    }

}
