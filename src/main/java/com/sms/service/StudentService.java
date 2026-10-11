package com.sms.service;

import com.sms.dto.StudentRequest;
import com.sms.entity.AppUser;
import com.sms.entity.Role;
import com.sms.entity.Student;
import com.sms.exception.DuplicateResourceException;
import com.sms.exception.ResourceNotFoundException;
import com.sms.repository.AppUserRepository;
import com.sms.repository.AttendanceRepository;
import com.sms.repository.MarkRepository;
import com.sms.repository.StudentRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final AppUserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final MarkRepository markRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(StudentRepository studentRepository,
                          AppUserRepository userRepository,
                          AttendanceRepository attendanceRepository,
                          MarkRepository markRepository,
                          PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.attendanceRepository = attendanceRepository;
        this.markRepository = markRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<Student> getAll(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return studentRepository.findAll(Sort.by("name"));
        }
        return studentRepository.search(keyword.trim());
    }

    @Transactional(readOnly = true)
    public Student getById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));
    }

    @Transactional(readOnly = true)
    public Student getByUsername(String username) {
        return studentRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found"));
    }

    @Transactional
    public Student create(StudentRequest request) {
        String roll = request.rollNumber().trim();

        if (studentRepository.existsByRollNumber(roll) || userRepository.existsByUsername(roll)) {
            throw new DuplicateResourceException("Roll number already exists: " + roll);
        }
        if (studentRepository.existsByEmail(request.email().trim())) {
            throw new DuplicateResourceException("Email already exists: " + request.email().trim());
        }

        // The roll number is the student's login username.
        // If no password is given, the roll number is used as the first password.
        String rawPassword = (request.password() == null || request.password().isBlank())
                ? roll : request.password();

        AppUser user = new AppUser();
        user.setUsername(roll);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(Role.STUDENT);

        Student student = new Student();
        student.setRollNumber(roll);
        applyFields(student, request);
        student.setUser(user);

        return studentRepository.save(student);
    }

    @Transactional
    public Student update(Long id, StudentRequest request) {
        Student student = getById(id);

        String email = request.email().trim();
        if (!email.equalsIgnoreCase(student.getEmail()) && studentRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Email already exists: " + email);
        }

        // Roll number (username) is not changed on update.
        applyFields(student, request);

        if (request.password() != null && !request.password().isBlank() && student.getUser() != null) {
            student.getUser().setPassword(passwordEncoder.encode(request.password()));
        }
        return studentRepository.save(student);
    }

    @Transactional
    public void delete(Long id) {
        Student student = getById(id);
        attendanceRepository.deleteAllForStudent(id);
        markRepository.deleteAllForStudent(id);
        studentRepository.delete(student); // also removes the linked login account
    }

    @Transactional(readOnly = true)
    public Map<String, Long> stats() {
        Map<String, Long> stats = new LinkedHashMap<>();
        stats.put("totalStudents", studentRepository.count());
        stats.put("totalDepartments", studentRepository.countDepartments());
        stats.put("attendanceRecords", attendanceRepository.count());
        stats.put("markRecords", markRepository.count());
        return stats;
    }

    private void applyFields(Student student, StudentRequest r) {
        student.setName(r.name().trim());
        student.setEmail(r.email().trim());
        student.setPhone(blankToNull(r.phone()));
        student.setDepartment(r.department().trim());
        student.setCourse(blankToNull(r.course()));
        student.setAcademicYear(r.academicYear());
        student.setDateOfBirth(r.dateOfBirth());
        student.setAddress(blankToNull(r.address()));
    }

    private String blankToNull(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }
}
