package com.example.bookmanager.controller;

import com.example.bookmanager.model.Book;
import com.example.bookmanager.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository repository;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String list(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "") String category) {

        Sort sort = Sort.by(sortField);
        sort = "asc".equalsIgnoreCase(sortDir) ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        String keywordClean = keyword == null ? "" : keyword.trim();
        String categoryClean = category == null ? "" : category.trim();

        Page<Book> bookPage = repository.search(keywordClean, categoryClean, pageable);

        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookPage.getTotalPages());
        model.addAttribute("totalItems", bookPage.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", "asc".equalsIgnoreCase(sortDir) ? "desc" : "asc");
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        model.addAttribute("categories", repository.findDistinctCategory());

        return "list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String showForm(Model model) {
        model.addAttribute("book", new Book());
        return "form";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String save(@ModelAttribute Book book, @RequestParam(required = false) MultipartFile image) {
        if (book != null) {
            if (image != null && !image.isEmpty()) {
                try {
                    String uploadDir = "src/main/resources/static/images";
                    File dir = new File(uploadDir);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    String filename = UUID.randomUUID() + "-" + image.getOriginalFilename();
                    Path filepath = Paths.get(uploadDir, filename);
                    Files.write(filepath, image.getBytes());

                    book.setImageUrl(filename);
                } catch (Exception e) {
                    e.printStackTrace();
                    book.setImageUrl("default-book.svg");
                }
            } else if (book.getImageUrl() == null || book.getImageUrl().isEmpty()) {
                book.setImageUrl("default-book.svg");
            }
            repository.save(book);
        }
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String edit(@PathVariable Long id, Model model) {
        if (id != null) {
            Optional<Book> opt = repository.findById(id);
            if (opt.isPresent()) {
                model.addAttribute("book", opt.get());
                return "form";
            }
        }
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id) {
        if (id != null) {
            repository.deleteById(id);
        }
        return "redirect:/books";
    }
}