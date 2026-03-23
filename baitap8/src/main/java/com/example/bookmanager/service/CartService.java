package com.example.bookmanager.service;

import com.example.bookmanager.model.Book;
import com.example.bookmanager.model.CartItem;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Service
@SessionScope
public class CartService {

    private final List<CartItem> items = new ArrayList<>();

    public List<CartItem> getItems() {
        return items;
    }

    public void addItem(Book book, int quantity) {
        items.stream()
                .filter(ci -> ci.getBook().getId().equals(book.getId()))
                .findFirst()
                .ifPresentOrElse(ci -> ci.setQuantity(ci.getQuantity() + quantity),
                        () -> items.add(new CartItem(book, quantity)));
    }

    public void updateItemQuantity(Long bookId, int quantity) {
        items.stream()
                .filter(ci -> ci.getBook().getId().equals(bookId))
                .findFirst()
                .ifPresent(ci -> ci.setQuantity(Math.max(1, quantity)));
    }

    public void removeItem(Long bookId) {
        items.removeIf(ci -> ci.getBook().getId().equals(bookId));
    }

    public void clear() {
        items.clear();
    }

    public int getTotalQuantity() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public double getTotalAmount() {
        return items.stream().mapToDouble(CartItem::getSubtotal).sum();
    }
}