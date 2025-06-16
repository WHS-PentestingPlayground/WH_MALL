package com.whs.dev2.controller;

import com.whs.dev2.dto.PostRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/board")
public class PostPageController {

    @Value("${API_SERVER_URL}")
    private String apiServerUrl;

    @GetMapping("/posts")
    public String listPosts(Model model) {
        model.addAttribute("apiServerUrl", apiServerUrl);
        return "postList"; // JSP에서 /api/posts 호출
    }

    @GetMapping("/newPost")
    public String showNewPostForm(Model model) {
        model.addAttribute("postRequestDto", new PostRequestDto());
        model.addAttribute("apiServerUrl", apiServerUrl);
        return "postForm"; // JSP에서 /api/posts 호출
    }

    @GetMapping("/posts/{id}")
    public String showPostDetail(@PathVariable Long id, Model model) {
        model.addAttribute("postId", id);
        model.addAttribute("apiServerUrl", apiServerUrl);
        return "postDetail"; // JSP에서 /api/posts/{id} 호출
    }

    @GetMapping("/editPost")
    public String showEditPostForm(@RequestParam("id") Long id, Model model) {
        model.addAttribute("postId", id);
        model.addAttribute("apiServerUrl", apiServerUrl);
        return "postEditForm"; // JSP에서 /api/posts/{id} 호출
    }
}
