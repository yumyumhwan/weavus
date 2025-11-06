package com.example.logi.controller;

import com.example.logi.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class DashboardController {

    private final OrderService orderService;

    public DashboardController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        long currentOrderCount = orderService.countCurrentOrders();
        long shippedCount      = orderService.countShippedAndStockUpdated();

        model.addAttribute("currentOrderCount", currentOrderCount);
        model.addAttribute("shippedCount", shippedCount);

        return "admin/dashboard";   // dashboard.html
    }
}