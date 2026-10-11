package com.sms.repository;

import com.sms.entity.Mark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MarkRepository extends JpaRepository<Mark, Long> {

    List<Mark> findByStudent_IdOrderBySubjectAscIdAsc(Long studentId);

    @Modifying
    @Query("DELETE FROM Mark m WHERE m.student.id = :studentId")
    void deleteAllForStudent(@Param("studentId") Long studentId);
}
