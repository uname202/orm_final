package com.example.orm.service;

import com.example.orm.model.Course;
import com.example.orm.model.Enrollment;
import com.example.orm.model.EnrollmentStatus;
import com.example.orm.model.User;
import com.example.orm.repository.CourseRepository;
import com.example.orm.repository.EnrollmentRepository;
import com.example.orm.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             UserRepository userRepository,
                             CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional
    public Enrollment enrollStudent(Long studentId, Long courseId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));

        return enrollmentRepository.findByUserAndCourse(student, course)
                .orElseGet(() -> {
                    Enrollment enrollment = new Enrollment();
                    enrollment.setUser(student);
                    enrollment.setCourse(course);
                    enrollment.setEnrollDate(LocalDate.now());
                    enrollment.setStatus(EnrollmentStatus.ACTIVE);
                    return enrollmentRepository.save(enrollment);
                });
    }

    @Transactional
    public Enrollment completeEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found: " + enrollmentId));
        enrollment.setStatus(EnrollmentStatus.COMPLETED);
        return enrollmentRepository.save(enrollment);
    }
}
