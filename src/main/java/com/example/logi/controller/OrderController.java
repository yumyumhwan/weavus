package com.example.logi.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.logi.domain.order.Order;
import com.example.logi.domain.order.OrderStatus;
import com.example.logi.service.OrderService;

@Controller
@RequestMapping("/admin/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 6번 페이지: 주문 접수 폼 + 주문 목록
//    @GetMapping
//    public String list(
//            @RequestParam(value = "status", required = false) String status,
//            Model model,
//            @ModelAttribute("message") String message,
//            @ModelAttribute("error") String error
//    ) {
//        if (status != null && !status.isBlank()) {
//            try {
//                // 문자열을 Enum으로 변환
//                var orderStatus = OrderStatus.valueOf(status);
//                model.addAttribute("orders", orderService.findByStatus(orderStatus));
//            } catch (IllegalArgumentException e) {
//                // 존재하지 않는 상태일 경우 전체 목록
//                model.addAttribute("orders", orderService.findAll());
//                model.addAttribute("error", "無効な状態が指定されました。");
//            }
//        } else {
//            model.addAttribute("orders", orderService.findAll());
//        }
//
//        model.addAttribute("selectedStatus", status);  // 현재 선택값 유지용
//        model.addAttribute("statuses", OrderStatus.values());  // select 박스용
//
//        return "admin/orders/orderList";
//    }
 // ✅ 주문 목록 + 상태 필터 + 페이징
    @GetMapping
    public String list(@RequestParam(name = "status", required = false) String status,
                       @RequestParam(name = "page", defaultValue = "1") int page,
                       Model model) {

        int pageSize = 5; // 한 페이지당 표시할 주문 수

        // 서비스에서 페이징 처리
        Page<Order> orderPage = orderService.getPage(status, page, pageSize);

        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("orderPage", orderPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);

        // 상태 필터 select용 데이터
        model.addAttribute("statuses", OrderStatus.values());
        model.addAttribute("selectedStatus",
                (status == null || status.isBlank()) ? "ALL" : status);

        return "admin/orders/orderList";
    }

    // 주문 접수 처리 (SKU + 수량)
    @PostMapping("/new")   // ★ HTML의 action "/admin/orders/new"와 일치
    public String create(@RequestParam("sku") String sku,
                         @RequestParam("qty") Integer qty,
                         RedirectAttributes ra) {

        try {
            orderService.createOrder(sku.trim(), qty);
            ra.addFlashAttribute("message", "注文を受付しました。");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());  // ★ 키 이름: error
        }

        return "redirect:/admin/orders";
    }

    // 상태 한 단계 진행 (출고준비 → 출고処理 → 在庫更新)
    @PostMapping("/{id}/next")
    public String nextStatus(@PathVariable("id") Long id,
                             RedirectAttributes ra) {
        try {
            orderService.advanceStatus(id);
            ra.addFlashAttribute("message", "注文状態を更新しました。");
        } catch (IllegalStateException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/orders";
    }
}