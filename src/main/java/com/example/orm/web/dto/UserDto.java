package com.example.orm.web.dto;

public record UserDto(
        Long id,
        String name,
        String email
) {
}
