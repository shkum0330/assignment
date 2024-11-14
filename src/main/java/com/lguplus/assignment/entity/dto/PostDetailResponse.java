package com.lguplus.assignment.entity.dto;

import com.lguplus.assignment.entity.Post;
import lombok.Builder;
import lombok.Data;

@Data
public class PostDetailResponse {
    private Long postId;
    private String title;
    private String content;
    private String authorUsername;
    private Long viewCount;

    @Builder
    public PostDetailResponse(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.authorUsername = post.getMember().getUsername();
        this.viewCount=post.getViewCount();
    }
}
