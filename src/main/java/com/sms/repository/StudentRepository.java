package com.sms.repository;

import com.sms.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByRollNumber(String rollNumber);

    boolean existsByEmail(String email);

    Optional<Student> findByUserUsername(String username);

    @Query("SELECT s FROM Student s WHERE "
            + "LOWER(s.name) LIKE LOWER(CONCAT('%', :kw, '%')) OR "
            + "LOWER(s.rollNumber) LIKE LOWER(CONCAT('%', :kw, '%')) OR "
            + "LOWER(s.email) LIKE LOWER(CONCAT('%', :kw, '%')) OR "
            + "LOWER(s.department) LIKE LOWER(CONCAT('%', :kw, '%')) "
            + "ORDER BY s.name")
    List<Student> search(@Param("kw") String keyword);

    @Query("SELECT COUNT(DISTINCT s.department) FROM Student s")
    long countDepartments();
}
