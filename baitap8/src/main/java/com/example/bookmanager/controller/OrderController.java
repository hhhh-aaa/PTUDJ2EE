package com.example.bookmanager.controller;

import com.example.bookmanager.service.CartService;
import com.example.bookmanager.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/checkout")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String checkout(Model model) {
        if (cartService.getItems().isEmpty()) {
            model.addAttribute("message", "Giỏ hàng trống, không thể đặt hàng.");
            return "cart/list";
        }

        var order = orderService.placeOrder(cartService);
        model.addAttribute("order", order);
        model.addAttribute("message", "Đặt hàng thành công. Mã đơn hàng: " + order.getId());
        return "cart/checkout";
    }
}