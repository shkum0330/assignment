package com.lguplus.assignment.controller;

import com.lguplus.assignment.entity.dto.request.PostRequest;
import com.lguplus.assignment.entity.dto.response.PostResponse;
import com.lguplus.assignment.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestHeader("Authorization") String token,
                                                   @RequestBody @Valid PostRequest postRequest) {
        PostResponse postResponse = postService.createPost(token, postRequest);
        return ResponseEntity.ok(postResponse);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(@RequestHeader("Authorization") String token,
                                                      @PathVariable Long postId,
                                                      @RequestBody @Valid PostRequest postRequest) {
        PostResponse postResponse = postService.updatePost(token, postId, postRequest);
        return ResponseEntity.ok(postResponse);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@RequestHeader("Authorization") String token,
                                             @PathVariable Long postId) {
        postService.deletePost(token, postId);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }

    // 게시글 전체 조회
    @GetMapping
    public ResponseEntity<?> getAllPosts(@PageableDefault(size = 5, sort = "postId",
            direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "lastPostId", required = false) Long lastPostId) {
        return ResponseEntity.ok(postService.getAllPosts(lastPostId, pageable));
    }

    // 게시글 단건 조회
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@RequestHeader("Authorization") String token, @PathVariable("postId") Long postId) {
        return ResponseEntity.ok(postService.getPostDetails(token,postId));
    }

    // 외부 게시글 등록
    @PostMapping("/import-external")
    public ResponseEntity<String> importExternalPosts(@RequestHeader("Authorization") String token) {
        postService.importExternalPosts(token);
        return ResponseEntity.ok("외부 게시글이 성공적으로 등록되었습니다.");
    }
}
