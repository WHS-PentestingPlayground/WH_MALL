package com.whs.dev2.controller;

import com.whs.dev2.dto.PostRequestDto;
import com.whs.dev2.dto.PostResponseDto;
import com.whs.dev2.entity.Post;
import com.whs.dev2.entity.User;
import com.whs.dev2.jwt.JwtUtil;
import com.whs.dev2.service.PostService;
import com.whs.dev2.service.UserService;

// FreeMarker 관련 임포트 추가
import freemarker.core.TemplateClassResolver;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
// import org.springframework.ui.Model; // @RestController 에서는 보통 Model 대신 ResponseEntity 사용
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer; // FreeMarkerConfigurer 임포트

import javax.annotation.PostConstruct; // @PostConstruct 임포트 (javax 또는 jakarta)

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostApiController {

    private final PostService postService;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final FreeMarkerConfigurer freemarkerConfigurer; // FreeMarkerConfigurer 주입

    // FreeMarker의 T() 함수를 통한 클래스 로딩을 허용하기 위한 설정 (주의: 보안 취약점 유발)
    // 이 코드를 추가하면 공격자가 템플릿에서 java.lang.Runtime 등을 직접 호출할 수 있게 됩니다.
    // 실습을 위해 추가하지만, 실제 프로덕션 코드에서는 절대 사용하면 안 됩니다.
    @PostConstruct // 애플리케이션 시작 시 실행
    public void setupFreeMarker() {
        Configuration cfg = freemarkerConfigurer.getConfiguration();
        // UNRESTRICTED_RESOLVER를 설정하여 템플릿에서 모든 자바 클래스에 접근 가능하게 함
        cfg.setNewBuiltinClassResolver(TemplateClassResolver.UNRESTRICTED_RESOLVER);
        System.out.println("[SSTI Vulnerability Warning]: FreeMarker TemplateClassResolver set to UNRESTRICTED_RESOLVER for lab purposes. DO NOT USE IN PRODUCTION!");
    }


    // 모든 게시글 조회
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    // 게시글 상세 조회 - SSTI 취약점 유발을 위해 변경
    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable Long id) {
        try {
            PostResponseDto post = postService.getPost(id);

            // --- SSTI 취약점 유발 핵심 로직 변경 시작 ---
            // 게시글 내용(content)을 FreeMarker 템플릿으로 렌더링
            Configuration cfg = freemarkerConfigurer.getConfiguration();
            StringWriter writer = new StringWriter();
            Map<String, Object> model = new HashMap<>(); // 템플릿에 전달할 데이터 (여기서는 비어있어도 됨)

            try {
                // 사용자가 입력한 게시글 content를 직접 템플릿으로 파싱하여 렌더링
                // 이 부분이 FreeMarker SSTI의 취약점 포인트
                Template template = new Template("postContentTemplate", post.getContent(), cfg);
                template.process(model, writer);
                String renderedContent = writer.toString();
                post.setContent(renderedContent); // 렌더링된 내용으로 DTO 업데이트

            } catch (IOException | TemplateException e) {
                // 템플릿 렌더링 오류 발생 시 (예: 잘못된 FreeMarker 구문)
                System.err.println("FreeMarker 렌더링 오류: " + e.getMessage());
                // 오류가 발생해도 원본 내용을 반환하거나, 특정 오류 메시지를 반환하도록 처리
                // 여기서는 오류 메시지를 포함한 응답을 반환
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 내용 렌더링 중 오류 발생: " + e.getMessage());
            }
            // --- SSTI 취약점 유발 핵심 로직 변경 끝 ---

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
            PostResponseDto post = postService.createPost(dto, user, null);
            return ResponseEntity.ok(post);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
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
            PostResponseDto post = postService.createPost(dto, user, file);
            return ResponseEntity.ok(post);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
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
