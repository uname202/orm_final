package com.example.orm.repository;

import com.example.orm.model.Course;
import com.example.orm.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTeacher(User teacher);

    @EntityGraph(attributePaths = {"category", "teacher", "tags"})
    List<Course> findAll();

    @EntityGraph(attributePaths = {"category", "teacher", "tags", "modules", "modules.lessons"})
    Optional<Course> findWithModulesById(Long id);
}
