package com.whs.dev2.controller;

import com.whs.dev2.dto.PostRequestDto;
import com.whs.dev2.entity.Post;
import com.whs.dev2.entity.User;
import com.whs.dev2.service.PostService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class PostPageController {

    private final PostService postService;

    @GetMapping("/posts")
    public String listPosts(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        return "postList";
    }

    @GetMapping("/newPost")
    public String showNewPostForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("loginUserId", user.getId());
        model.addAttribute("loginUsername", user.getUsername());
        return "postForm";
    }

    @GetMapping("/posts/{id}")
    public String showPostDetail(@PathVariable Long id, Model model, HttpSession session) {
        Post post = postService.getPost(id);
        if (post == null) {
            return "redirect:/board/posts";
        }

        User author = post.getUser();
        model.addAttribute("board", post);
        model.addAttribute("username", author != null ? author.getUsername() : "Unknown");

        User loginUser = (User) session.getAttribute("user");
        if (loginUser != null) {
            model.addAttribute("loginUserId", loginUser.getId());
        }

        return "postDetail";
    }

    @PostMapping("/newPost")
    public String createPost(@ModelAttribute PostRequestDto dto,
                             @RequestParam(value = "file", required = false) MultipartFile file,
                             HttpSession session) {
        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null) {
            return "redirect:/login";
        }

        dto.setAuthor(loginUser.getUsername());
        postService.createPost(dto, loginUser, file);
        return "redirect:/board/posts";
    }

    @PostMapping("/deletePost")
    public String deletePost(@RequestParam("id") Long id, HttpSession session) {
        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null) {
            return "redirect:/login";
        }

        postService.deletePost(id, loginUser);
        return "redirect:/board/posts";
    }

    @GetMapping("/editPost")
    public String showEditPostForm(@RequestParam("id") Long id, Model model, HttpSession session) {
        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null) {
            return "redirect:/login";
        }

        Post post = postService.getPost(id);
        if (!post.getUser().getId().equals(loginUser.getId())) {
            return "redirect:/board/posts";
        }

        model.addAttribute("post", post);
        model.addAttribute("loginUserId", loginUser.getId());
        model.addAttribute("loginUsername", loginUser.getUsername());
        return "postEditForm";
    }

    @PostMapping("/editPost")
    public String editPost(@RequestParam("id") Long id,
                           @ModelAttribute PostRequestDto dto,
                           @RequestParam(value = "file", required = false) MultipartFile file,
                           HttpSession session) {
        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null) {
            return "redirect:/login";
        }

        postService.updatePost(id, dto, loginUser, file);
        return "redirect:/board/posts/" + id;
    }


    @GetMapping("/download")
    public void downloadFile(@RequestParam("filename") String filename, HttpServletResponse response) throws IOException {
        String uploadDir = System.getProperty("user.dir") + File.separator + "uploads";
        File file = new File(uploadDir, filename);

        if (!file.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("파일을 찾을 수 없습니다.");
            return;
        }


        String originalFileName = filename.substring(filename.indexOf("_") + 1);
        String encodedName = URLEncoder.encode(originalFileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedName + "\"");
        response.setContentLengthLong(file.length());

        try (FileInputStream in = new FileInputStream(file);
             OutputStream out = response.getOutputStream()) {
            in.transferTo(out);
            out.flush();
        }
    }

}
