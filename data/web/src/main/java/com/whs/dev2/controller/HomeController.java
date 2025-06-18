package com.whs.dev2.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Value("${API_SERVER_URL}")
    private String apiServerUrl;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("apiServerUrl", apiServerUrl);
        return "index";
    }
}
