package com.whs.dev2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CouponPageController {
    @GetMapping("/coupon-issue")
    public String couponIssuePage() {
        return "coupon-issue";
    }
} 