package com.example.bookmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/**
 * Main entry point for the Book Manager Spring Boot application.  Running this
 * class will bootstrap the embedded server and initialize all Spring
 * components found via component‑scan under the
 * <code>com.example.bookmanager</code> package.
 */
public class BookManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookManagerApplication.class, args);
    }
}