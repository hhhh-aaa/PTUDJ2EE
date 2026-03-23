package com.example.bookmanager.service;

import com.example.bookmanager.model.CartItem;
import com.example.bookmanager.model.OrderDetail;
import com.example.bookmanager.model.PurchaseOrder;
import com.example.bookmanager.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private PurchaseOrderRepository orderRepository;

    public PurchaseOrder placeOrder(CartService cartService) {
        PurchaseOrder order = new PurchaseOrder();
        order.setTotalAmount(cartService.getTotalAmount());
        order.setTotalQuantity(cartService.getTotalQuantity());

        for (CartItem item : cartService.getItems()) {
            OrderDetail detail = new OrderDetail();
            detail.setBook(item.getBook());
            detail.setQuantity(item.getQuantity());
            detail.setPrice(item.getBook().getPrice());
            order.addDetail(detail);
        }

        PurchaseOrder saved = orderRepository.save(order);
        cartService.clear();

        return saved;
    }
}
