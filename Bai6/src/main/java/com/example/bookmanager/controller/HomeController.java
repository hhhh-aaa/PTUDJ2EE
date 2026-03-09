package com.example.bookmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Simple controller that redirects the root path to the book list.  This
 * ensures that visiting "http://localhost:8081/" (or after login) lands on
 * a meaningful page instead of producing a 404.
 */
@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "redirect:/books";
    }
}
