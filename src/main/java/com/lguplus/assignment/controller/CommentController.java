package com.lguplus.assignment.controller;

import com.lguplus.assignment.entity.dto.response.CommentResponse;
import com.lguplus.assignment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Slf4j
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

    // 댓글과 대댓글 조회
    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentResponse>> getComments(@PageableDefault(size = 10, sort = "commentId",
            direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable Long postId,
            @RequestParam(value = "lastCommentId", required = false) Long lastCommentId) {
        log.info("lastcommentid = {}",lastCommentId);
        log.info("postid = {}",postId);
        List<CommentResponse> comments = commentService.getCommentsByPost(postId, lastCommentId,pageable);
        return ResponseEntity.ok(comments);
    }
}
