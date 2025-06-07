package com.whs.dev2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserPageController {

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register"; // 폼 제출 시 /api/users/register 호출
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // 폼 제출 시 /api/users/login 호출
    }
}
