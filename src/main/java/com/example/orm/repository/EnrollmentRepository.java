package com.example.orm.repository;

import com.example.orm.model.Enrollment;
import com.example.orm.model.User;
import com.example.orm.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByUserAndCourse(User user, Course course);
}
