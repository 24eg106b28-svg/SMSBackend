package com.sms.controller;

import com.sms.dto.LoginRequest;
import com.sms.dto.LoginResponse;
import com.sms.entity.AppUser;
import com.sms.entity.Role;
import com.sms.entity.Student;
import com.sms.repository.AppUserRepository;
import com.sms.repository.StudentRepository;
import com.sms.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AppUserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(AppUserRepository userRepository,
                          StudentRepository studentRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        Optional<AppUser> found = userRepository.findByUsername(request.username().trim());

        if (found.isEmpty() || !passwordEncoder.matches(request.password(), found.get().getPassword())) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid username or password"));
        }

        AppUser user = found.get();
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

        String displayName = user.getUsername();
        Long studentId = null;
        if (user.getRole() == Role.STUDENT) {
            Optional<Student> student = studentRepository.findByUserUsername(user.getUsername());
            if (student.isPresent()) {
                displayName = student.get().getName();
                studentId = student.get().getId();
            }
        }

        return ResponseEntity.ok(new LoginResponse(token, user.getUsername(), user.getRole().name(),
                displayName, studentId));
    }
}
