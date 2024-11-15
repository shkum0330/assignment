package com.lguplus.assignment.service;

import com.lguplus.assignment.entity.Comment;
import com.lguplus.assignment.entity.Member;
import com.lguplus.assignment.entity.Post;
import com.lguplus.assignment.entity.dto.response.CommentResponse;
import com.lguplus.assignment.global.exception.CommentNotFoundException;
import com.lguplus.assignment.global.exception.PostNotFoundException;
import com.lguplus.assignment.global.exception.UnauthorizedException;
import com.lguplus.assignment.global.jwt.JwtUtil;
import com.lguplus.assignment.repository.CommentRepository;
import com.lguplus.assignment.repository.MemberRepository;
import com.lguplus.assignment.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public void addComment(Long postId, String token, String content) {
        Long memberId = jwtUtil.validateToken(token);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UnauthorizedException("사용자를 찾을 수 없습니다."));

        Comment comment = Comment.builder()
                .post(post)
                .member(member)
                .content(content)
                .isDeleted(false)
                .build();

        commentRepository.save(comment);
    }

    @Transactional
    public void addReply(Long parentCommentId, String token, String content) {
        Long memberId = jwtUtil.validateToken(token);
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new CommentNotFoundException("댓글을 찾을 수 없습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UnauthorizedException("사용자를 찾을 수 없습니다."));

        Comment reply = Comment.builder()
                .post(parentComment.getPost()) // 부모 댓글의 게시글
                .parent(parentComment)
                .member(member)
                .content(content)
                .isDeleted(false)
                .build();

        commentRepository.save(reply);
    }

    @Transactional
    public void deleteCommentsByPost(Post post) {
        commentRepository.deleteByPost(post); // 게시글과 연관된 댓글 삭제
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByPost(Long postId, Long lastCommentId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findCommentsByPostIdAndLastCommentId(postId, lastCommentId, pageable);
        log.info("postId = {}",postId);
        return comments.stream()
                .map(CommentResponse::new)
                .toList();
    }
}
