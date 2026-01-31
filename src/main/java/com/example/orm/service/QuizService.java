package com.example.orm.service;

import com.example.orm.model.*;
import com.example.orm.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final CourseModuleRepository moduleRepository;
    private final CourseRepository courseRepository;
    private final QuestionRepository questionRepository;
    private final AnswerOptionRepository answerOptionRepository;
    private final QuizSubmissionRepository quizSubmissionRepository;
    private final UserRepository userRepository;

    public QuizService(QuizRepository quizRepository,
                       CourseModuleRepository moduleRepository,
                       CourseRepository courseRepository,
                       QuestionRepository questionRepository,
                       AnswerOptionRepository answerOptionRepository,
                       QuizSubmissionRepository quizSubmissionRepository,
                       UserRepository userRepository) {
        this.quizRepository = quizRepository;
        this.moduleRepository = moduleRepository;
        this.courseRepository = courseRepository;
        this.questionRepository = questionRepository;
        this.answerOptionRepository = answerOptionRepository;
        this.quizSubmissionRepository = quizSubmissionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Quiz createQuizForModule(Long moduleId, String title, Integer timeLimit) {
        CourseModule module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new IllegalArgumentException("Module not found: " + moduleId));
        Quiz quiz = new Quiz();
        quiz.setModule(module);
        quiz.setTitle(title);
        quiz.setTimeLimit(timeLimit);
        return quizRepository.save(quiz);
    }

    @Transactional
    public Quiz createQuizForCourse(Long courseId, String title, Integer timeLimit) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));
        Quiz quiz = new Quiz();
        quiz.setCourse(course);
        quiz.setTitle(title);
        quiz.setTimeLimit(timeLimit);
        return quizRepository.save(quiz);
    }

    @Transactional
    public Question addQuestion(Long quizId, String text, QuestionType type) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found: " + quizId));
        Question question = new Question();
        question.setQuiz(quiz);
        question.setText(text);
        question.setType(type);
        return questionRepository.save(question);
    }

    @Transactional
    public AnswerOption addAnswerOption(Long questionId, String text, boolean isCorrect) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question not found: " + questionId));
        AnswerOption option = new AnswerOption();
        option.setQuestion(question);
        option.setText(text);
        option.setCorrect(isCorrect);
        return answerOptionRepository.save(option);
    }

    @Transactional
    public QuizSubmission submitQuiz(Long quizId, Long studentId, Integer score) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found: " + quizId));
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));
        QuizSubmission submission = new QuizSubmission();
        submission.setQuiz(quiz);
        submission.setStudent(student);
        submission.setScore(score);
        submission.setTakenAt(LocalDateTime.now());
        return quizSubmissionRepository.save(submission);
    }
}
