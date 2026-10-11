package com.sms.service;

import com.sms.dto.AttendanceRequest;
import com.sms.dto.MarkRequest;
import com.sms.entity.Attendance;
import com.sms.entity.Mark;
import com.sms.entity.Student;
import com.sms.exception.ResourceNotFoundException;
import com.sms.repository.AttendanceRepository;
import com.sms.repository.MarkRepository;
import com.sms.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AcademicService {

    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;
    private final MarkRepository markRepository;

    public AcademicService(StudentRepository studentRepository,
                           AttendanceRepository attendanceRepository,
                           MarkRepository markRepository) {
        this.studentRepository = studentRepository;
        this.attendanceRepository = attendanceRepository;
        this.markRepository = markRepository;
    }

    // ---------- Attendance ----------
    @Transactional(readOnly = true)
    public List<Attendance> getAttendance(Long studentId) {
        return attendanceRepository.findByStudent_IdOrderByAttendanceDateDesc(studentId);
    }

    @Transactional
    public Attendance addAttendance(Long studentId, AttendanceRequest request) {
        Student student = findStudent(studentId);
        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setSubject(request.subject().trim());
        attendance.setAttendanceDate(request.attendanceDate());
        attendance.setStatus(request.status());
        return attendanceRepository.save(attendance);
    }

    @Transactional
    public void deleteAttendance(Long id) {
        if (!attendanceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Attendance record not found with id " + id);
        }
        attendanceRepository.deleteById(id);
    }

    // ---------- Marks ----------
    @Transactional(readOnly = true)
    public List<Mark> getMarks(Long studentId) {
        return markRepository.findByStudent_IdOrderBySubjectAscIdAsc(studentId);
    }

    @Transactional
    public Mark addMark(Long studentId, MarkRequest request) {
        if (request.marksObtained() > request.maxMarks()) {
            throw new IllegalArgumentException("Marks obtained cannot be greater than maximum marks");
        }
        Student student = findStudent(studentId);
        Mark mark = new Mark();
        mark.setStudent(student);
        mark.setSubject(request.subject().trim());
        mark.setExamType(request.examType().trim());
        mark.setMarksObtained(request.marksObtained());
        mark.setMaxMarks(request.maxMarks());
        return markRepository.save(mark);
    }

    @Transactional
    public void deleteMark(Long id) {
        if (!markRepository.existsById(id)) {
            throw new ResourceNotFoundException("Mark record not found with id " + id);
        }
        markRepository.deleteById(id);
    }

    private Student findStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));
    }
}
