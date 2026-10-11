package com.sms.controller;

import com.sms.entity.Attendance;
import com.sms.entity.Mark;
import com.sms.entity.Student;
import com.sms.service.AcademicService;
import com.sms.service.StudentService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoints for a logged-in student. A student can only see their own data:
 * the student is always resolved from the login token, never from a request parameter.
 */
@RestController
@RequestMapping("/api/student")
public class StudentPortalController {

    private final StudentService studentService;
    private final AcademicService academicService;

    public StudentPortalController(StudentService studentService, AcademicService academicService) {
        this.studentService = studentService;
        this.academicService = academicService;
    }

    @GetMapping("/me")
    public Student me(Authentication authentication) {
        return studentService.getByUsername(authentication.getName());
    }

    @GetMapping("/attendance")
    public List<Attendance> attendance(Authentication authentication) {
        Student student = studentService.getByUsername(authentication.getName());
        return academicService.getAttendance(student.getId());
    }

    @GetMapping("/marks")
    public List<Mark> marks(Authentication authentication) {
        Student student = studentService.getByUsername(authentication.getName());
        return academicService.getMarks(student.getId());
    }
}
