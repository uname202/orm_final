package com.example.orm.repository;

import com.example.orm.model.Lesson;
import com.example.orm.model.CourseModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByModule(CourseModule module);
}
