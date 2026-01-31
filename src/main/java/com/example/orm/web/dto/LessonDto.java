package com.example.orm.web.dto;

public record LessonDto(
        Long id,
        String title,
        String content,
        String videoUrl
) {
}
