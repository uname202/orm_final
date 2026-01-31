package com.example.orm.service;

import com.example.orm.model.*;
import com.example.orm.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CourseModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;
    private final TagRepository tagRepository;

    public CourseService(CourseRepository courseRepository,
                         CategoryRepository categoryRepository,
                         UserRepository userRepository,
                         CourseModuleRepository moduleRepository,
                         LessonRepository lessonRepository,
                         TagRepository tagRepository) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.moduleRepository = moduleRepository;
        this.lessonRepository = lessonRepository;
        this.tagRepository = tagRepository;
    }

    @Transactional
    public Course createCourse(String title,
                               String description,
                               Long categoryId,
                               Long teacherId,
                               String duration,
                               LocalDate startDate) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + categoryId));
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found: " + teacherId));

        Course course = new Course();
        course.setTitle(title);
        course.setDescription(description);
        course.setCategory(category);
        course.setTeacher(teacher);
        course.setDuration(duration);
        course.setStartDate(startDate);
        return courseRepository.save(course);
    }

    @Transactional
    public CourseModule addModule(Long courseId, String title, int orderIndex, String description) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));
        CourseModule module = new CourseModule();
        module.setCourse(course);
        module.setTitle(title);
        module.setOrderIndex(orderIndex);
        module.setDescription(description);
        return moduleRepository.save(module);
    }

    @Transactional
    public Lesson addLesson(Long moduleId, String title, String content, String videoUrl) {
        CourseModule module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new IllegalArgumentException("Module not found: " + moduleId));
        Lesson lesson = new Lesson();
        lesson.setModule(module);
        lesson.setTitle(title);
        lesson.setContent(content);
        lesson.setVideoUrl(videoUrl);
        return lessonRepository.save(lesson);
    }

    @Transactional
    public Course addTag(Long courseId, String tagName) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));

        Tag tag = tagRepository.findByNameIgnoreCase(tagName)
                .orElseGet(() -> {
                    Tag created = new Tag();
                    created.setName(tagName);
                    return tagRepository.save(created);
                });

        course.getTags().add(tag);
        return courseRepository.save(course);
    }
}
