package com.jmfs.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Register Request Data Transfer Object")
public record RegisterRequestDTO(String username, String password, String email) {
}
