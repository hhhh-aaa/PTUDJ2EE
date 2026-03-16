package com.example.bookmanager.controller;

import com.example.bookmanager.model.Course;
import com.example.bookmanager.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/courses")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCourseController {

    @Autowired
    private CourseRepository repository;

    @GetMapping
    public String list(Model model) {
        List<Course> courses = repository.findAll();
        model.addAttribute("courses", courses);
        return "admin/course-list";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("course", new Course());
        return "admin/course-form";
    }

    @PostMapping
    public String save(@ModelAttribute Course course) {
        repository.save(course);
        return "redirect:/admin/courses";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        return repository.findById(id)
                .map(course -> {
                    model.addAttribute("course", course);
                    return "admin/course-form";
                })
                .orElse("redirect:/admin/courses");
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/admin/courses";
    }
}
