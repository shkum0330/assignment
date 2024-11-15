package com.lguplus.assignment.controller;

import com.lguplus.assignment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{postId}")
    public ResponseEntity<String> addComment(@PathVariable Long postId,
                                             @RequestHeader("Authorization") String token,
                                             @RequestParam String content) {
        commentService.addComment(postId, token, content);
        return ResponseEntity.ok("댓글이 등록되었습니다.");
    }

    @PostMapping("/reply/{parentCommentId}")
    public ResponseEntity<String> addReply(@PathVariable Long parentCommentId,
                                           @RequestHeader("Authorization") String token,
                                           @RequestParam String content) {
        commentService.addReply(parentCommentId, token, content);
        return ResponseEntity.ok("대댓글이 등록되었습니다.");
    }
}
