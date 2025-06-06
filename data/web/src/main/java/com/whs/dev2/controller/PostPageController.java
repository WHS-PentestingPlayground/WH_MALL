package com.whs.dev2.controller;

// import com.whs.dev2.entity.Post;
// import com.whs.dev2.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Controller
public class PostPageController {
    // private final PostService postService;

    @GetMapping("/posts")
    public String listPosts(Model model) {
        // model.addAttribute("posts", postService.getAllPosts());
        return "postList";
    }

    @GetMapping("/post/new")
    public String showPostForm() {
        return "postForm"; // → /WEB-INF/views/postForm.jsp
    }

    @PostMapping("/post")
    public String createPost(@RequestParam String title,
                             @RequestParam String content,
                             @RequestParam String author) {
        // Post post = new Post();
        // post.setTitle(title);
        // post.setContent(content);
        // post.setAuthor(author);
        return "redirect:/posts";
    }
}