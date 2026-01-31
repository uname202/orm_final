package com.example.orm.repository;

import com.example.orm.model.Submission;
import com.example.orm.model.User;
import com.example.orm.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    Optional<Submission> findByAssignmentAndStudent(Assignment assignment, User student);
}
