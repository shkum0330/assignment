package com.lguplus.assignment.service;

import com.lguplus.assignment.entity.Member;
import com.lguplus.assignment.entity.Post;
import com.lguplus.assignment.entity.dto.PostRequest;
import com.lguplus.assignment.entity.dto.PostResponse;
import com.lguplus.assignment.global.exception.PostNotFoundException;
import com.lguplus.assignment.global.exception.UnauthorizedPostAccessException;
import com.lguplus.assignment.global.jwt.JwtUtil;
import com.lguplus.assignment.repository.MemberRepository;
import com.lguplus.assignment.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public PostResponse createPost(String token, PostRequest postRequest) {
        Long memberId = jwtUtil.validateToken(token);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Post post = new Post(postRequest.getTitle(), postRequest.getContent(), member, 0L, false);
        Post savedPost = postRepository.save(post);

        return PostResponse.builder()
                .postId(savedPost.getPostId())
                .title(savedPost.getTitle())
                .content(savedPost.getContent())
                .authorUsername(member.getUsername())
                .build();
    }

    @Transactional
    public PostResponse updatePost(String token, Long postId, PostRequest postRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));
        Long memberId = jwtUtil.validateToken(token);
        if (!post.getMember().getMemberId().equals(memberId)) {
            throw new UnauthorizedPostAccessException("본인이 작성한 게시글만 수정할 수 있습니다.");
        }

        post.update(postRequest.getTitle(), postRequest.getContent());
        Post updatedPost = postRepository.save(post);

        return PostResponse.builder()
                .postId(updatedPost.getPostId())
                .title(updatedPost.getTitle())
                .content(updatedPost.getContent())
                .authorUsername(updatedPost.getMember().getUsername())
                .build();
    }

    @Transactional
    public void deletePost(String token, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        Long memberId = jwtUtil.validateToken(token);
        if (!post.getMember().getMemberId().equals(memberId)) {
            throw new UnauthorizedPostAccessException("본인이 작성한 게시글만 삭제할 수 있습니다.");
        }

        post.setDeleted(); // 소프트 삭제
    }
}
