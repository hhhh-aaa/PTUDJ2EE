package com.example.bookmanager.controller;

import com.example.bookmanager.model.Role;
import com.example.bookmanager.model.Student;
import com.example.bookmanager.repository.RoleRepository;
import com.example.bookmanager.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;

@Controller
public class AuthController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("student", new Student());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Student student) {
        if (studentRepository.findByUsername(student.getUsername()).isPresent()) {
            return "redirect:/register?error";
        }
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        Role defaultRole = roleRepository.findByName("ROLE_STUDENT").orElseGet(() -> {
            Role role = new Role("ROLE_STUDENT");
            return roleRepository.save(role);
        });
        student.setRoles(new HashSet<>());
        student.getRoles().add(defaultRole);
        studentRepository.save(student);
        return "redirect:/login?registered";
    }
}

