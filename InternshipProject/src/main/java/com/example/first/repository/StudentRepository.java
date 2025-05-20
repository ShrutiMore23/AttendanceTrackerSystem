package com.example.first.repository;

import com.example.first.model.student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<student, Long> {
    student findByEmail(String email);
}
