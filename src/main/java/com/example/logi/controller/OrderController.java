package com.example.logi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/orders")
public class OrderController {

    // 주문/출하 상태 목록
    @GetMapping
    public String list() {
        return "admin/orders/orderList";
    }

    // 출하 처리 (나중에 서비스 붙일 예정)
    @PostMapping("/{orderId}/ship")
    public String ship(@PathVariable Long orderId) {
        return "redirect:/admin/orders";
    }
}