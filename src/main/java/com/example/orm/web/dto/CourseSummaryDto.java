package com.example.orm.web.dto;

import java.time.LocalDate;
import java.util.List;

public record CourseSummaryDto(
        Long id,
        String title,
        String description,
        String categoryName,
        String teacherName,
        String duration,
        LocalDate startDate,
        List<String> tags
) {
}
