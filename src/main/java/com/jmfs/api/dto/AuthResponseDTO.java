package com.jmfs.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication Response Data Transfer Object")
public record AuthResponseDTO(Long id, String name, String token) {
}
