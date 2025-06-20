package com.whs.dev2.controller;

import com.whs.dev2.dto.PostRequestDto;
import com.whs.dev2.dto.PostResponseDto;
import com.whs.dev2.entity.Post;
import com.whs.dev2.entity.User;
import com.whs.dev2.jwt.JwtUtil;
import com.whs.dev2.service.PostService;
import com.whs.dev2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostApiController {

    private final PostService postService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    // 모든 게시글 조회
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    // 게시글 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable Long id) {
        try {
            Post post = postService.getPost(id);
            return ResponseEntity.ok(post);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시글을 찾을 수 없습니다.");
        }
    }

    // 게시글 작성 (JSON 요청)
    @PostMapping("/create")
    public ResponseEntity<?> createPost(
            @RequestBody PostRequestDto dto,
            @RequestHeader("Authorization") String authHeader) {

        User user = authenticate(authHeader);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        dto.setAuthor(user.getUsername());

        try {
            Post post = postService.createPost(dto, user, null);
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 생성 중 오류 발생");
        }
    }

    // 게시글 작성 (파일 포함 multipart)
    @PostMapping(path = "/create-with-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPostWithFile(
            @RequestPart("title") String title,
            @RequestPart("content") String content,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestHeader("Authorization") String authHeader) {

        User user = authenticate(authHeader);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        PostRequestDto dto = new PostRequestDto();
        dto.setTitle(title);
        dto.setContent(content);
        dto.setAuthor(user.getUsername());

        try {
            Post post = postService.createPost(dto, user, file);
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 생성 중 오류 발생");
        }
    }

    // 게시글 수정 (JSON)
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(
            @PathVariable Long id,
            @RequestBody PostRequestDto dto,
            @RequestHeader("Authorization") String authHeader) {

        User user = authenticate(authHeader);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        dto.setAuthor(user.getUsername());

        try {
            postService.updatePost(id, dto, user, null);
            return ResponseEntity.ok("게시글이 수정되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    // 게시글 수정 (파일 포함 multipart)
    @PostMapping(path = "/{id}/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> editPostWithFile(
            @PathVariable Long id,
            @RequestPart("title") String title,
            @RequestPart("content") String content,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestHeader("Authorization") String authHeader) {

        User user = authenticate(authHeader);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        PostRequestDto dto = new PostRequestDto();
        dto.setTitle(title);
        dto.setContent(content);
        dto.setAuthor(user.getUsername());

        try {
            postService.updatePost(id, dto, user, file);
            return ResponseEntity.ok("게시글이 수정되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        User user = authenticate(authHeader);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        try {
            postService.deletePost(id, user);
            return ResponseEntity.ok("게시글이 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    // 인증 메서드
    private User authenticate(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        String token = authHeader.substring(7);
        String username = jwtUtil.validateAndExtractUsername(token);
        if (username == null) return null;
        try {
            return userService.findByUsername(username);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
