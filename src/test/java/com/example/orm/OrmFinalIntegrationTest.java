package com.example.orm;

import com.example.orm.model.*;
import com.example.orm.repository.*;
import com.example.orm.service.AssignmentService;
import com.example.orm.service.CourseService;
import com.example.orm.service.EnrollmentService;
import com.example.orm.service.QuizService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class OrmFinalIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("orm_final_test")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void registerDataSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerOptionRepository answerOptionRepository;

    @Autowired
    private QuizSubmissionRepository quizSubmissionRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private QuizService quizService;

    @Test
    void fullWorkflowPersistsEntities() {
        User teacher = new User();
        teacher.setName("Alice Teacher");
        teacher.setEmail("alice@example.com");
        teacher.setRole(UserRole.TEACHER);
        teacher = userRepository.save(teacher);

        User student = new User();
        student.setName("Bob Student");
        student.setEmail("bob@example.com");
        student.setRole(UserRole.STUDENT);
        student = userRepository.save(student);

        Category category = new Category();
        category.setName("Programming");
        category = categoryRepository.save(category);

        Course course = courseService.createCourse(
                "Java ORM",
                "Hibernate and JPA",
                category.getId(),
                teacher.getId(),
                "8 weeks",
                LocalDate.now()
        );

        CourseModule module = courseService.addModule(course.getId(), "Intro", 1, "Basics");
        Lesson lesson = courseService.addLesson(module.getId(), "Lesson 1", "Content", null);

        Assignment assignment = assignmentService.createAssignment(
                lesson.getId(),
                "Homework 1",
                "Solve tasks",
                LocalDate.now().plusDays(7),
                100
        );

        Enrollment enrollment = enrollmentService.enrollStudent(student.getId(), course.getId());
        Submission submission = assignmentService.submitAssignment(assignment.getId(), student.getId(), "My answer");
        assignmentService.gradeSubmission(submission.getId(), 95, "Good job");

        Quiz quiz = quizService.createQuizForModule(module.getId(), "Quiz 1", 30);
        Question question = quizService.addQuestion(quiz.getId(), "What is JPA?", QuestionType.SINGLE_CHOICE);
        quizService.addAnswerOption(question.getId(), "API", true);
        quizService.addAnswerOption(question.getId(), "Database", false);
        QuizSubmission quizSubmission = quizService.submitQuiz(quiz.getId(), student.getId(), 85);

        assertThat(courseRepository.findById(course.getId())).isPresent();
        assertThat(assignmentRepository.findById(assignment.getId())).isPresent();
        assertThat(submissionRepository.findById(submission.getId())).isPresent();
        assertThat(enrollment.getId()).isNotNull();
        assertThat(quizRepository.findById(quiz.getId())).isPresent();
        assertThat(questionRepository.findById(question.getId())).isPresent();
        assertThat(answerOptionRepository.findAll()).hasSize(2);
        assertThat(quizSubmissionRepository.findById(quizSubmission.getId())).isPresent();
    }
}
