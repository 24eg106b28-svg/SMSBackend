package com.sms.controller;

import com.sms.dto.AttendanceRequest;
import com.sms.dto.MarkRequest;
import com.sms.dto.StudentRequest;
import com.sms.entity.Attendance;
import com.sms.entity.Mark;
import com.sms.entity.Student;
import com.sms.service.AcademicService;
import com.sms.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final StudentService studentService;
    private final AcademicService academicService;

    public AdminController(StudentService studentService, AcademicService academicService) {
        this.studentService = studentService;
        this.academicService = academicService;
    }

    // ---------- Dashboard ----------
    @GetMapping("/stats")
    public Map<String, Long> stats() {
        return studentService.stats();
    }

    // ---------- Students ----------
    @GetMapping("/students")
    public List<Student> getStudents(@RequestParam(required = false) String keyword) {
        return studentService.getAll(keyword);
    }

    @GetMapping("/students/{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentService.getById(id);
    }

    @PostMapping("/students")
    public ResponseEntity<Student> createStudent(@Valid @RequestBody StudentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.create(request));
    }

    @PutMapping("/students/{id}")
    public Student updateStudent(@PathVariable Long id, @Valid @RequestBody StudentRequest request) {
        return studentService.update(id, request);
    }

    @DeleteMapping("/students/{id}")
    public Map<String, String> deleteStudent(@PathVariable Long id) {
        studentService.delete(id);
        return Map.of("message", "Student deleted");
    }

    // ---------- Attendance ----------
    @GetMapping("/students/{id}/attendance")
    public List<Attendance> getAttendance(@PathVariable Long id) {
        studentService.getById(id);
        return academicService.getAttendance(id);
    }

    @PostMapping("/students/{id}/attendance")
    public ResponseEntity<Attendance> addAttendance(@PathVariable Long id,
                                                    @Valid @RequestBody AttendanceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(academicService.addAttendance(id, request));
    }

    @DeleteMapping("/attendance/{id}")
    public Map<String, String> deleteAttendance(@PathVariable Long id) {
        academicService.deleteAttendance(id);
        return Map.of("message", "Attendance record deleted");
    }

    // ---------- Marks ----------
    @GetMapping("/students/{id}/marks")
    public List<Mark> getMarks(@PathVariable Long id) {
        studentService.getById(id);
        return academicService.getMarks(id);
    }

    @PostMapping("/students/{id}/marks")
    public ResponseEntity<Mark> addMark(@PathVariable Long id, @Valid @RequestBody MarkRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(academicService.addMark(id, request));
    }

    @DeleteMapping("/marks/{id}")
    public Map<String, String> deleteMark(@PathVariable Long id) {
        academicService.deleteMark(id);
        return Map.of("message", "Mark record deleted");
    }
}
