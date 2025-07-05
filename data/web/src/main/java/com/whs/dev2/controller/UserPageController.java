package com.whs.dev2.controller;

import com.whs.dev2.dto.LoginRequestDto;
import com.whs.dev2.dto.RegisterRequestDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class UserPageController {

    @GetMapping("/register")
    public String showRegisterForm(@ModelAttribute RegisterRequestDto registerRequestDto) {
        // JSP에서 /api/users/register 호출
        return "register";
    }

    @GetMapping("/login")
    public String showLoginForm(@ModelAttribute LoginRequestDto loginRequestDto) {
        // JSP에서 /api/users/login 호출
        return "login";
    }
}
