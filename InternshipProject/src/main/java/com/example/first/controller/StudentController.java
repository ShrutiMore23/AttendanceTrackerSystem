package com.example.first.controller;

import jakarta.servlet.http.HttpSession;
import com.example.first.model.student;
import com.example.first.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    // Home Page
    @GetMapping("/")
    public String home() {
        return "home";  // This will render home.html
    }

    // Registration Form Page
    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(student student, Model model) {
        if (studentRepository.findByEmail(student.getEmail()) != null) {
            model.addAttribute("message", "This student already exists.");
        } else {
            student.setAttendance(false);
            studentRepository.save(student);
            model.addAttribute("message", "Registration successful!");
        }
        return "register";
    }

    // Login Form Page
    @GetMapping("/login")
    public String loginForm() {
        return "login";  // This will render login.html
    }


    @PostMapping("/login")
    public String login(@RequestParam String email, 
                        @RequestParam String password, 
                        Model model, 
                        HttpSession session) {
        // Find student by email
        student student = studentRepository.findByEmail(email);

        // Check if student exists and if the password matches
        if (student == null || !(student.getPassword().equals(password))) {
            model.addAttribute("error", "Invalid email or password!");
            return "login";  // Return to login page if student not found or password mismatch
        }

        // Store student in session after successful login
        session.setAttribute("student", student);  // Spring automatically manages HttpSession

        // Redirect to attendance page after login
        return "redirect:/attendance";  // Ensure this redirects to /attendance
    }


    // Attendance Page (Only accessible after login)
    @GetMapping("/attendance")
    public String attendancePage(HttpSession session, Model model) {
        // Get student from session
        student student = (student) session.getAttribute("student");

        if (student == null) {
            return "redirect:/login";  // Redirect to login page if session is null (not logged in)
        }

        // Add student to model to display on attendance page
        model.addAttribute("student", student);

        // Render attendance.html
        return "attendance";  
    }

//     Mark Attendance (POST request)
    @PostMapping("/attendance")
    public String markAttendance(HttpSession session) {
        student student = (student) session.getAttribute("student");
        if (student != null) {
            student.setAttendance(true);  // Mark attendance as true
            studentRepository.save(student);  // Save student data
            session.invalidate();  // Invalidate session after attendance
        }
        return "redirect:/";  // Redirect to home page after marking attendance
    }
    
   

}
