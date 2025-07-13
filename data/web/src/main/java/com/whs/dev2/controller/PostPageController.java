package com.whs.dev2.controller;

import com.whs.dev2.dto.PostRequestDto;
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
    public String listPosts(Model model) {
        return "postList"; // JSP에서 /api/posts 호출 (가맹점 문의)
    }

    @GetMapping("/newPost")
    public String showNewPostForm(Model model) {
        model.addAttribute("postRequestDto", new PostRequestDto());
        return "postForm"; // JSP에서 /api/posts 호출 (가맹점 문의)
    }

    @GetMapping("/posts/{id}")
    public String showPostDetail(@PathVariable Long id, Model model) {
        model.addAttribute("postId", id);
        return "postDetail"; // JSP에서 /api/posts/{id} 호출 (가맹점 문의)
    }
}
