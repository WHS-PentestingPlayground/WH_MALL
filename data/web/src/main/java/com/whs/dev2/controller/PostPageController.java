package com.whs.dev2.controller;

import com.whs.dev2.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/board")
public class PostPageController {

    @GetMapping("/posts")
    public String listPosts() {
        return "postList";
    }

    @GetMapping("/newPost")
    public String showNewPostForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        model.addAttribute("loginUserId", user.getId());
        model.addAttribute("loginUsername", user.getUsername());
        return "postForm";
    }

    @GetMapping("/posts/{id}")
    public String showPostDetail(@PathVariable Long id, Model model) {
        model.addAttribute("postId", id); // JS에서 API 호출
        return "postDetail";
    }

    @GetMapping("/editPost")
    public String showEditPostForm(@RequestParam("id") Long id, Model model, HttpSession session) {
        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null) return "redirect:/login";
        model.addAttribute("postId", id); // JS로 API에서 불러옴
        return "postEditForm";
    }
}
