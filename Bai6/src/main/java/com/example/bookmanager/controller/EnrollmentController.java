package com.example.bookmanager.controller;

import com.example.bookmanager.model.Course;
import com.example.bookmanager.model.Enrollment;
import com.example.bookmanager.model.Student;
import com.example.bookmanager.repository.CourseRepository;
import com.example.bookmanager.repository.EnrollmentRepository;
import com.example.bookmanager.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/enroll")
@PreAuthorize("hasRole('STUDENT')")
public class EnrollmentController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @PostMapping("/{courseId}")
    public String enroll(@PathVariable Long courseId, Authentication authentication) {
        String username = authentication.getName();
        Student student = studentRepository.findByUsername(username).orElse(null);
        if (student == null) {
            return "redirect:/login";
        }

        courseRepository.findById(courseId).ifPresent(course -> {
            boolean already = enrollmentRepository.findByStudentAndCourse(student, course).isPresent();
            if (!already) {
                Enrollment enrollment = new Enrollment(student, course);
                enrollment.setEnrollDate(LocalDateTime.now());
                enrollmentRepository.save(enrollment);
            }
        });
        return "redirect:/courses";
    }

    @GetMapping("/my-courses")
    public String myCourses(Model model, Authentication authentication) {
        String username = authentication.getName();
        Student student = studentRepository.findByUsername(username).orElse(null);
        if (student == null) {
            return "redirect:/login";
        }
        List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);
        model.addAttribute("enrollments", enrollments);
        return "my-courses";
    }
}
