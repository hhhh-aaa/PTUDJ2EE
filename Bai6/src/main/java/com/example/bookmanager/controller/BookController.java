package com.example.bookmanager.controller;

import com.example.bookmanager.model.Book;
import com.example.bookmanager.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
/**
 * Web MVC controller responsible for handling requests related to books.
 *
 * <p>Provides CRUD operations (list, create, update, delete) along with a
 * simple search feature that looks for titles containing a query string.
 * Thymeleaf templates in <code>src/main/resources/templates</code> are used
 * to render the HTML pages.</p>
 */
public class BookController {

    @Autowired
    private BookRepository repository;

    /**
     * Show the list of all books or the result of a title search.
     *
     * @param q    optional query string to filter books by title
     * @param model MVC model used to pass attributes to the view
     * @return name of the Thymeleaf template to render
     */
    @GetMapping
    public String list(@RequestParam(required = false) String q, Model model) {
        List<Book> books;
        if (q != null && !q.isBlank()) {
            books = repository.findByTitleContainingIgnoreCase(q);
            model.addAttribute("query", q);
        } else {
            books = repository.findAll();
        }
        model.addAttribute("books", books);
        return "list";
    }

    /**
     * Display an empty form for creating a new book.
     *
     * @param model MVC model used to supply a fresh {@link Book} instance
     * @return view name of the form template
     */
    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("book", new Book());
        return "form";
    }

    /**
     * Persist a book sent from the form.  Handles both new and edited books;
     * the presence of an ID determines whether an insert or update occurs.
     *
     * @param book bound model attribute containing submitted values
     * @return redirect to the book list on success
     */
    @PostMapping
    public String save(@ModelAttribute Book book) {
        repository.save(book);
        return "redirect:/books";
    }

    /**
     * Load an existing book and pre‑populate the form for editing.
     *
     * @param id    identifier of the book to edit
     * @param model MVC model for view attributes
     * @return form view when found; otherwise redirect back to list
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        return repository.findById(id)
                .map(book -> {
                    model.addAttribute("book", book);
                    return "form";
                })
                .orElse("redirect:/books");
    }

    /**
     * Remove the book with the given id.  The link on the list page prompts
     * the user for confirmation before invoking this endpoint.
     *
     * @param id identifier of the book to delete
     * @return redirect to the updated list
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/books";
    }
}