package com.lguplus.assignment.controller;

import com.lguplus.assignment.entity.dto.PostRequest;
import com.lguplus.assignment.entity.dto.PostResponse;
import com.lguplus.assignment.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestHeader("Authorization") String token,
                                                   @RequestBody PostRequest postRequest) {
        PostResponse postResponseDTO = postService.createPost(token, postRequest);
        return ResponseEntity.ok(postResponseDTO);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(@RequestHeader("Authorization") String token,
                                                      @PathVariable Long postId,
                                                      @RequestBody PostRequest postRequestDTO) {
        PostResponse postResponseDTO = postService.updatePost(token, postId, postRequestDTO);
        return ResponseEntity.ok(postResponseDTO);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@RequestHeader("Authorization") String token,
                                             @PathVariable Long postId) {
        postService.deletePost(token, postId);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }
}
