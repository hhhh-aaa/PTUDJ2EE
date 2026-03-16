package com.example.bookmanager.config;

import com.example.bookmanager.model.Course;
import com.example.bookmanager.model.Role;
import com.example.bookmanager.model.Student;
import com.example.bookmanager.repository.CourseRepository;
import com.example.bookmanager.repository.RoleRepository;
import com.example.bookmanager.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
                           StudentRepository studentRepository,
                           CourseRepository courseRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));
        Role studentRole = roleRepository.findByName("ROLE_STUDENT")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_STUDENT")));

        if (studentRepository.findByUsername("admin").isEmpty()) {
            Student admin = new Student();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setEmail("admin@example.com");
            admin.setRoles(new HashSet<>());
            admin.getRoles().add(adminRole);
            studentRepository.save(admin);
        }

        if (courseRepository.count() == 0) {
            courseRepository.save(new Course("Spring Boot Basics", "https://via.placeholder.com/120", 3, "Dr. Nguyen"));
            courseRepository.save(new Course("Database Systems", "https://via.placeholder.com/120", 4, "Prof. Tran"));
            courseRepository.save(new Course("Web Development", "https://via.placeholder.com/120", 3, "Ms. Le"));
            courseRepository.save(new Course("Algorithms", "https://via.placeholder.com/120", 4, "Dr. Pham"));
            courseRepository.save(new Course("Software Engineering", "https://via.placeholder.com/120", 3, "Dr. Ho"));
            courseRepository.save(new Course("Data Structures", "https://via.placeholder.com/120", 4, "Mr. Bui"));
        }
    }
}
