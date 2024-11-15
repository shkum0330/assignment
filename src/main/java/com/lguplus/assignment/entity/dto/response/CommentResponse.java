package com.lguplus.assignment.entity.dto.response;

import com.lguplus.assignment.entity.Comment;
import lombok.Data;

@Data
public class CommentResponse {
    private Long commentId;
    private String content;
    private Long parentId; // 부모 댓글 ID
    private String author; // 작성자

    public CommentResponse(Comment comment) {
        this.commentId = comment.getCommentId();
        this.content = comment.getContent();
        this.parentId = (comment.getParent() != null) ? comment.getParent().getCommentId() : null;
        this.author = comment.getMember().getUsername();
    }
}
