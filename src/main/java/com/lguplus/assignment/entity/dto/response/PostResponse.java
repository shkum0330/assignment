package com.lguplus.assignment.entity.dto.response;

import com.lguplus.assignment.entity.Post;
import lombok.Data;

@Data
public class PostResponse {
    private Long postId;
    private String title;
    private String authorUsername;
    private Long viewCount;


    public PostResponse(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.authorUsername = post.getMember().getUsername();
        this.viewCount=post.getViewCount();
    }
}
