package com.example.bookmanager.controller;

import com.example.bookmanager.model.Course;
import com.example.bookmanager.model.Enrollment;
import com.example.bookmanager.model.Student;
import com.example.bookmanager.repository.CourseRepository;
import com.example.bookmanager.repository.EnrollmentRepository;
import com.example.bookmanager.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class CourseController {

    private static final int PAGE_SIZE = 5;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @GetMapping({"/", "/home", "/courses"})
    public String listCourses(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            Model model,
            Authentication authentication
    ) {
        Page<Course> courses;
        if (q != null && !q.isBlank()) {
            courses = courseRepository.findByNameContainingIgnoreCase(q, PageRequest.of(page, PAGE_SIZE));
        } else {
            courses = courseRepository.findAll(PageRequest.of(page, PAGE_SIZE));
        }

        model.addAttribute("courses", courses.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", courses.getTotalPages());
        model.addAttribute("query", q);

        // if user is student, determine enrolled course ids for the list
        model.addAttribute("enrolledCourseIds", List.<Long>of());
        if (authentication != null && authentication.isAuthenticated()) {
            Optional<Student> studentOpt = studentRepository.findByUsername(authentication.getName());
            if (studentOpt.isPresent()) {
                Student student = studentOpt.get();
                List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);
                model.addAttribute("enrolledCourseIds",
                        enrollments.stream().map(e -> e.getCourse().getId()).toList());
            }
        }

        return "courses";
    }
}

