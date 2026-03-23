package com.example.bookmanager.controller;

import com.example.bookmanager.model.Book;
import com.example.bookmanager.repository.BookRepository;
import com.example.bookmanager.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CartService cartService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String viewCart(Model model) {
        model.addAttribute("items", cartService.getItems());
        model.addAttribute("totalQuantity", cartService.getTotalQuantity());
        model.addAttribute("totalAmount", cartService.getTotalAmount());
        return "cart/list";
    }

    @PostMapping("/add/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String addToCart(@PathVariable Long id, @RequestParam(defaultValue = "1") int quantity) {
        bookRepository.findById(id).ifPresent(book -> cartService.addItem(book, Math.max(1, quantity)));
        return "redirect:/cart";
    }

    @PostMapping("/update")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String updateCart(@RequestParam Long bookId, @RequestParam int quantity) {
        cartService.updateItemQuantity(bookId, quantity);
        return "redirect:/cart";
    }

    @GetMapping("/remove/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String removeFromCart(@PathVariable Long id) {
        cartService.removeItem(id);
        return "redirect:/cart";
    }

    @GetMapping("/clear")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String clearCart() {
        cartService.clear();
        return "redirect:/cart";
    }
}