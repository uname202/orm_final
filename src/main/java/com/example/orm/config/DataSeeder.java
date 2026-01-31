package com.example.orm.config;

import com.example.orm.model.*;
import com.example.orm.repository.*;
import com.example.orm.service.AssignmentService;
import com.example.orm.service.CourseService;
import com.example.orm.service.EnrollmentService;
import com.example.orm.service.QuizService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedData(UserRepository userRepository,
                               ProfileRepository profileRepository,
                               CategoryRepository categoryRepository,
                               CourseReviewRepository courseReviewRepository,
                               CourseService courseService,
                               EnrollmentService enrollmentService,
                               AssignmentService assignmentService,
                               QuizService quizService) {
        return args -> {
            if (userRepository.count() > 0) {
                return;
            }

            User teacher = new User();
            teacher.setName("Alice Teacher");
            teacher.setEmail("alice.teacher@example.com");
            teacher.setRole(UserRole.TEACHER);
            teacher = userRepository.save(teacher);

            Profile teacherProfile = new Profile();
            teacherProfile.setUser(teacher);
            teacherProfile.setBio("Senior Java instructor");
            teacherProfile.setAvatarUrl("https://example.com/avatars/alice.png");
            profileRepository.save(teacherProfile);

            User student = new User();
            student.setName("Bob Student");
            student.setEmail("bob.student@example.com");
            student.setRole(UserRole.STUDENT);
            student = userRepository.save(student);

            Category category = new Category();
            category.setName("Programming");
            category = categoryRepository.save(category);

            Course course = courseService.createCourse(
                    "Java ORM Bootcamp",
                    "Build a full ORM learning platform with Spring Boot.",
                    category.getId(),
                    teacher.getId(),
                    "6 weeks",
                    LocalDate.now().plusDays(3)
            );
            courseService.addTag(course.getId(), "Java");
            courseService.addTag(course.getId(), "JPA");
            courseService.addTag(course.getId(), "Beginner");

            CourseModule module = courseService.addModule(course.getId(), "Introduction", 1, "Core ORM concepts");
            Lesson lesson = courseService.addLesson(module.getId(), "JPA Basics", "Entity mapping and relationships", null);

            Assignment assignment = assignmentService.createAssignment(
                    lesson.getId(),
                    "Homework 1",
                    "Model the domain entities.",
                    LocalDate.now().plusDays(7),
                    100
            );

            enrollmentService.enrollStudent(student.getId(), course.getId());
            assignmentService.submitAssignment(assignment.getId(), student.getId(), "My solution");

            Quiz quiz = quizService.createQuizForModule(module.getId(), "Quiz 1", 20);
            Question question = quizService.addQuestion(quiz.getId(), "What does JPA stand for?", QuestionType.SINGLE_CHOICE);
            quizService.addAnswerOption(question.getId(), "Java Persistence API", true);
            quizService.addAnswerOption(question.getId(), "Java Performance API", false);
            quizService.submitQuiz(quiz.getId(), student.getId(), 90);

            CourseReview review = new CourseReview();
            review.setCourse(course);
            review.setStudent(student);
            review.setRating(5);
            review.setComment("Great intro course.");
            review.setCreatedAt(LocalDateTime.now());
            courseReviewRepository.save(review);
        };
    }
}
