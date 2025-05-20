package com.example.first.service;

import com.example.first.model.student;
import com.example.first.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AttendanceResetService {

    @Autowired
    private StudentRepository studentRepository;

    // This method will reset attendance every day at midnight
    @Scheduled(cron = "0 0 0 * * ?")  // Cron expression for daily reset at midnight
    public void resetAttendance() {
        // Get all students
        Iterable<student> students = studentRepository.findAll();

        // Loop through all students and reset their attendance
        for (student s : students) {
            s.setAttendance(false);  // Reset attendance
            studentRepository.save(s);  // Save the student with updated attendance
        }

        System.out.println("Attendance has been reset for all students.");
    }
}
