package com.example.orm.web;

import com.example.orm.model.Course;
import com.example.orm.model.CourseModule;
import com.example.orm.model.Lesson;
import com.example.orm.model.Tag;
import com.example.orm.model.UserRole;
import com.example.orm.repository.CategoryRepository;
import com.example.orm.repository.CourseRepository;
import com.example.orm.repository.UserRepository;
import com.example.orm.web.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CatalogController {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CatalogController(CourseRepository courseRepository,
                             CategoryRepository categoryRepository,
                             UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/courses")
    @Transactional(readOnly = true)
    public List<CourseSummaryDto> listCourses() {
        return courseRepository.findAll().stream()
                .map(this::toSummary)
                .toList();
    }

    @GetMapping("/courses/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<CourseDetailDto> getCourse(@PathVariable Long id) {
        return courseRepository.findWithModulesById(id)
                .map(course -> ResponseEntity.ok(toDetail(course)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/categories")
    public List<CategoryDto> listCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> new CategoryDto(category.getId(), category.getName()))
                .toList();
    }

    @GetMapping("/teachers")
    public List<UserDto> listTeachers() {
        return userRepository.findByRole(UserRole.TEACHER).stream()
                .map(user -> new UserDto(user.getId(), user.getName(), user.getEmail()))
                .toList();
    }

    private CourseSummaryDto toSummary(Course course) {
        List<String> tags = course.getTags().stream()
                .map(Tag::getName)
                .sorted()
                .toList();
        return new CourseSummaryDto(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getCategory() != null ? course.getCategory().getName() : null,
                course.getTeacher() != null ? course.getTeacher().getName() : null,
                course.getDuration(),
                course.getStartDate(),
                tags
        );
    }

    private CourseDetailDto toDetail(Course course) {
        List<String> tags = course.getTags().stream()
                .map(Tag::getName)
                .sorted()
                .toList();

        List<ModuleDto> modules = course.getModules().stream()
                .sorted(Comparator.comparingInt(CourseModule::getOrderIndex))
                .map(module -> new ModuleDto(
                        module.getId(),
                        module.getTitle(),
                        module.getOrderIndex(),
                        module.getDescription(),
                        module.getLessons().stream()
                                .sorted(Comparator.comparing(Lesson::getId))
                                .map(lesson -> new LessonDto(
                                        lesson.getId(),
                                        lesson.getTitle(),
                                        lesson.getContent(),
                                        lesson.getVideoUrl()
                                ))
                                .toList()
                ))
                .toList();

        return new CourseDetailDto(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getCategory() != null ? course.getCategory().getName() : null,
                course.getTeacher() != null ? course.getTeacher().getName() : null,
                course.getDuration(),
                course.getStartDate(),
                tags,
                modules
        );
    }
}
