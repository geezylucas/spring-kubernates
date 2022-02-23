package com.geezylucas.app;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentRepository studentRepository;

    @GetMapping // Get request that returns all students
    public ResponseEntity<List<Student>> getAllStudents() {
        return new ResponseEntity<>(studentRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}") // Get request that returns a specific student with the provided id
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return new ResponseEntity<>(studentRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Student with id " + id + " not found")), HttpStatus.OK);
    }

    @PostMapping// Post request that creates a new student in the db
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        return new ResponseEntity<>(studentRepository.save(student), HttpStatus.CREATED);
    }

    @PutMapping("/{id}") // Put request that replaces an old student
    public ResponseEntity<Student> replaceStudent(@RequestBody Student newStudent, @PathVariable Long id) {
        Student studentReplaced = studentRepository.findById(id)
                .map(student -> {
                    student.setName(newStudent.getName());
                    student.setCourse(newStudent.getCourse());
                    student.setRegNo(newStudent.getRegNo());
                    return studentRepository.save(student);
                }).orElseGet(() -> {
                    newStudent.setId(id);
                    return studentRepository.save(newStudent);
                });

        return new ResponseEntity<>(studentReplaced, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        studentRepository.deleteById(id);
        return new ResponseEntity<>("Student deleted", HttpStatus.NO_CONTENT);
    }

}
