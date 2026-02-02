package com.example.bai4.service;

import com.example.bai4.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final List<Product> products = new ArrayList<>();
    private Long currentId = 1L;

    public List<Product> getAll() {
        return products;
    }

    public void add(Product product) {
        product.setId(currentId++);
        products.add(product);
    }

    public Product getById(Long id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void update(Product product) {
        Product old = getById(product.getId());
        if (old != null) {
            old.setName(product.getName());
            old.setCategory(product.getCategory());
            old.setPrice(product.getPrice());
            old.setImageUrl(product.getImageUrl());
        }
    }

    public void delete(Long id) {
        products.removeIf(p -> p.getId().equals(id));
    }
}
