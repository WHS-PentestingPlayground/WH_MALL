package com.whs.dev2.controller;

import com.whs.dev2.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final JwtUtil jwtUtil;

    @Autowired
    public GlobalControllerAdvice(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @ModelAttribute("username")
    public String getUsername(@CookieValue(name = "jwtToken", required = false) String token) {
        if (token != null) {
            return jwtUtil.validateAndExtractUsername(token);
        }
        return null;
    }
} 