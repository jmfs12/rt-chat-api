package com.jmfs.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Login Request Data Transfer Object")
public record LoginRequestDTO(String username, String password) {
}
