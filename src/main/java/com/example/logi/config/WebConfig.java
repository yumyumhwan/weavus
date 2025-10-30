package com.example.logi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 루트 → 로그인으로 리다이렉트
//        registry.addRedirectViewController("/", "/login");

        // 1) 로그인
//        registry.addViewController("/login").setViewName("login");

        // 2) 대시보드
//        registry.addViewController("/admin/dashboard").setViewName("admin/dashboard");

        // 3) 상품/재고 목록 (파일명: productList.html)
//        registry.addViewController("/admin/products").setViewName("admin/products/productList");

        // 4) 상품 신규 등록 (new.html)
//        registry.addViewController("/admin/products/new").setViewName("admin/products/new");

        // 5) 상품 수정 (edit.html) — 임시 라우팅
//        registry.addViewController("/admin/products/edit").setViewName("admin/products/edit");

        // 6) 주문/출하 상태 목록 (파일명: orderList.html)
//        registry.addViewController("/admin/orders").setViewName("admin/orders/orderList");
    }
}