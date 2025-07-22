package com.whs.dev2.service;

import com.whs.dev2.dto.PostSummaryDto;
import com.whs.dev2.dto.PostRequestDto;
import com.whs.dev2.dto.PostResponseDto;
import com.whs.dev2.entity.Post;
import com.whs.dev2.entity.User;
import com.whs.dev2.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAllByOrderByIdDesc().stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<PostSummaryDto> getAllPostSummaries() {
        return postRepository.findAllByOrderByIdDesc().stream()
                .map(PostSummaryDto::new)
                .collect(Collectors.toList());
    }

    public PostResponseDto getPost(Long id) {
        Post post = findPostById(id);
        return new PostResponseDto(post);
    }

    
    @Transactional
    public PostResponseDto createPost(PostRequestDto dto, User user, String tokenRole) {
        // JWT 토큰의 role로 admin 권한 체크
        if (!"partner".equals(tokenRole)) {
            throw new IllegalArgumentException("공지사항 작성 권한이 없습니다. partner 권한이 필요합니다.");
        }

        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setUser(user);

        Post savedPost = postRepository.save(post);
        return new PostResponseDto(savedPost);
    }


    @Transactional
    public void deletePost(Long id, User user) {
        Post post = findPostById(id);
        if (!post.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("게시글을 삭제할 권한이 없습니다.");
        }

        postRepository.delete(post);
    }

    private Post findPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
    }


}
