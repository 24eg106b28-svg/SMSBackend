package com.sms.config;

import com.sms.dto.AttendanceRequest;
import com.sms.dto.MarkRequest;
import com.sms.dto.StudentRequest;
import com.sms.entity.AppUser;
import com.sms.entity.AttendanceStatus;
import com.sms.entity.Role;
import com.sms.entity.Student;
import com.sms.repository.AppUserRepository;
import com.sms.repository.StudentRepository;
import com.sms.service.AcademicService;
import com.sms.service.StudentService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Runs at start-up: creates the default admin account and one demo student
 * (only when they do not exist yet).
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final AppUserRepository userRepository;
    private final StudentRepository studentRepository;
    private final StudentService studentService;
    private final AcademicService academicService;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(AppUserRepository userRepository,
                           StudentRepository studentRepository,
                           StudentService studentService,
                           AcademicService academicService,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.studentService = studentService;
        this.academicService = academicService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!userRepository.existsByUsername("admin")) {
            AppUser admin = new AppUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            System.out.println(">> Default admin created: admin / admin123");
        }

        if (studentRepository.count() == 0) {
            Student demo = studentService.create(new StudentRequest(
                    "CS2024001", "Rahul Sharma", "rahul.sharma@example.com", "9876543210",
                    "Computer Science", "B.Tech", 2, LocalDate.of(2004, 5, 14),
                    "Hyderabad, Telangana", "student123"));

            Long id = demo.getId();
            String[] subjects = {"Data Structures", "Database Systems", "Java Programming"};
            LocalDate start = LocalDate.now().minusDays(6);
            for (int i = 0; i < 6; i++) {
                for (String subject : subjects) {
                    AttendanceStatus status = ((i + subject.length()) % 5 == 0)
                            ? AttendanceStatus.ABSENT : AttendanceStatus.PRESENT;
                    academicService.addAttendance(id,
                            new AttendanceRequest(subject, start.plusDays(i), status));
                }
            }
            academicService.addMark(id, new MarkRequest("Data Structures", "Midterm", 78.0, 100.0));
            academicService.addMark(id, new MarkRequest("Database Systems", "Midterm", 85.0, 100.0));
            academicService.addMark(id, new MarkRequest("Java Programming", "Midterm", 91.0, 100.0));
            System.out.println(">> Demo student created: CS2024001 / student123");
        }
    }
}
