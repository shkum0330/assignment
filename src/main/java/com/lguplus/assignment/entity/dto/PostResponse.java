package com.lguplus.assignment.entity.dto;

import com.lguplus.assignment.entity.Post;
import lombok.Builder;
import lombok.Data;

@Data
public class PostResponse {
    private Long postId;
    private String title;
    private String authorUsername;

    @Builder
    public PostResponse(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.authorUsername = post.getMember().getUsername();
    }
}
