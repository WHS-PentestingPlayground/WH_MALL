package com.whs.dev2.dto;

import lombok.Getter;
import com.whs.dev2.entity.Post;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    @Setter
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String fileName;
    private String author;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.author = post.getUser().getUsername();
        this.updatedAt = post.getUpdatedAt() != null ? post.getUpdatedAt() : post.getCreatedAt();
        this.fileName = post.getFileName();
    }

}