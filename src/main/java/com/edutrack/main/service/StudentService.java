package com.edutrack.main.service;




import com.edutrack.main.model.Student;
import com.edutrack.main.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // Calculate grade based on percentage
    private String calculateGrade(Double percentage) {
        if (percentage >= 90) return "A+";
        else if (percentage >= 80) return "A";
        else if (percentage >= 70) return "B";
        else if (percentage >= 60) return "C";
        else if (percentage >= 50) return "D";
        else return "F";
    }

    // Calculate totals before saving
    private Student calculateResults(Student student) {
        double total = student.getMathMarks() 
                     + student.getScienceMarks() 
                     + student.getEnglishMarks();
        double percentage = (total / 300) * 100;
        student.setTotalMarks(total);
        student.setPercentage(Math.round(percentage * 100.0) / 100.0);
        student.setGrade(calculateGrade(percentage));
        return student;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }

    public Student addStudent(Student student) {
        if (studentRepository.existsByRollNumber(student.getRollNumber())) {
            throw new RuntimeException("Roll number already exists!");
        }
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }
        return studentRepository.save(calculateResults(student));
    }

    public Student updateStudent(Long id, Student updatedStudent) {
        Student existing = getStudentById(id);
        existing.setName(updatedStudent.getName());
        existing.setEmail(updatedStudent.getEmail());
        existing.setRollNumber(updatedStudent.getRollNumber());
        existing.setMathMarks(updatedStudent.getMathMarks());
        existing.setScienceMarks(updatedStudent.getScienceMarks());
        existing.setEnglishMarks(updatedStudent.getEnglishMarks());
        return studentRepository.save(calculateResults(existing));
    }

    public void deleteStudent(Long id) {
        getStudentById(id); // throws if not found
        studentRepository.deleteById(id);
    }
}