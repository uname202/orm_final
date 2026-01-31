package com.example.orm.repository;

import com.example.orm.model.CourseModule;
import com.example.orm.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseModuleRepository extends JpaRepository<CourseModule, Long> {
    List<CourseModule> findByCourse(Course course);
}
