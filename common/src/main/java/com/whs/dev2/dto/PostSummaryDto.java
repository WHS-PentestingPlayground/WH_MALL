package com.whs.dev2.dto;

import java.time.LocalDateTime;
import com.whs.dev2.entity.Post;

public class PostSummaryDto {
    private Long id;
    private String title;
    private String author;
    private LocalDateTime createdAt;

    public PostSummaryDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.author = post.getUser().getUsername();
        this.createdAt = post.getCreatedAt();
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public LocalDateTime getCreatedAt() { return createdAt; }
} 
