package com.lguplus.assignment.entity.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class PostResponse {
    private Long postId;
    private String title;
    private String content;
    private String authorUsername;

    @Builder
    public PostResponse(Long postId, String title, String content, String authorUsername) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.authorUsername = authorUsername;
    }
}
