package com.example.logi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @GetMapping({"/", "/login"})
    public String loginForm() {
        return "login"; // templates/login.html
    }

    // 학습용: 로그인 검증 없이 바로 대시보드로
//    @PostMapping("/login")
//    public String doLogin() {
//        return "redirect:/admin/dashboard";
//    }
//
//    @GetMapping("/logout")
//    public String logout() {
//        return "redirect:/login";
//    }
}