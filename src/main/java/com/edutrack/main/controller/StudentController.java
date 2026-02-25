package com.edutrack.main.controller;



import com.edutrack.main.model.Student;
import com.edutrack.main.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.edutrack.main.model.User;
import com.edutrack.main.repository.UserRepository;
import com.edutrack.main.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentService studentService;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.addStudent(student));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        return ResponseEntity.ok(studentService.updateStudent(id, student));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok("Student deleted successfully");
    }
    
    
    @GetMapping("/my-result")
    public ResponseEntity<?> getMyResult(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String rollNumber = user.getRollNumber().trim().toLowerCase();

        List<Student> students = studentService.getAllStudents();
        Student myResult = students.stream()
                .filter(s -> s.getRollNumber().trim().toLowerCase().equals(rollNumber))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Result not found for roll: " + rollNumber));

        return ResponseEntity.ok(myResult);
    }
}
