package com.example.bookmanager.model;

import jakarta.persistence.*;

@Entity
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private PurchaseOrder order;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private int quantity;
    private double price;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PurchaseOrder getOrder() { return order; }
    public void setOrder(PurchaseOrder order) { this.order = order; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getSubtotal() { return quantity * price; }
}