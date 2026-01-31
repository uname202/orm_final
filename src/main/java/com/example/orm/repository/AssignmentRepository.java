package com.example.orm.repository;

import com.example.orm.model.Assignment;
import com.example.orm.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByLesson(Lesson lesson);
}
