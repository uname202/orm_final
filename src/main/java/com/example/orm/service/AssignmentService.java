package com.example.orm.service;

import com.example.orm.model.Assignment;
import com.example.orm.model.Lesson;
import com.example.orm.model.Submission;
import com.example.orm.model.User;
import com.example.orm.repository.AssignmentRepository;
import com.example.orm.repository.LessonRepository;
import com.example.orm.repository.SubmissionRepository;
import com.example.orm.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final LessonRepository lessonRepository;
    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;

    public AssignmentService(AssignmentRepository assignmentRepository,
                             LessonRepository lessonRepository,
                             SubmissionRepository submissionRepository,
                             UserRepository userRepository) {
        this.assignmentRepository = assignmentRepository;
        this.lessonRepository = lessonRepository;
        this.submissionRepository = submissionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Assignment createAssignment(Long lessonId, String title, String description, LocalDate dueDate, Integer maxScore) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found: " + lessonId));

        Assignment assignment = new Assignment();
        assignment.setLesson(lesson);
        assignment.setTitle(title);
        assignment.setDescription(description);
        assignment.setDueDate(dueDate);
        assignment.setMaxScore(maxScore);
        return assignmentRepository.save(assignment);
    }

    @Transactional
    public Submission submitAssignment(Long assignmentId, Long studentId, String content) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("Assignment not found: " + assignmentId));
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));

        return submissionRepository.findByAssignmentAndStudent(assignment, student)
                .orElseGet(() -> {
                    Submission submission = new Submission();
                    submission.setAssignment(assignment);
                    submission.setStudent(student);
                    submission.setSubmittedAt(LocalDateTime.now());
                    submission.setContent(content);
                    return submissionRepository.save(submission);
                });
    }

    @Transactional
    public Submission gradeSubmission(Long submissionId, Integer score, String feedback) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found: " + submissionId));
        submission.setScore(score);
        submission.setFeedback(feedback);
        return submissionRepository.save(submission);
    }
}
