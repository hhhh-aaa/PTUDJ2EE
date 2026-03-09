package com.example.bookmanager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
/**
 * Domain entity that represents a book record stored in the database.
 *
 * <p>This simple POJO is managed by JPA (Jakarta Persistence) and is the
 * primary model used throughout the application.  It contains the book's
 * identifier, title, author and price.  Getter/setter methods are provided
 * for use by Spring MVC and persistence frameworks.</p>
 */
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // title of the book, e.g. "Effective Java"
    private String title;
    // name of the author or authors
    private String author;
    // retail price in local currency
    private double price;

    public Book() {
    }

    public Book(String title, String author, double price) {
        this.title = title;
        this.author = author;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}