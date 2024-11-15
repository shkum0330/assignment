package com.lguplus.assignment.service;

import com.lguplus.assignment.entity.Comment;
import com.lguplus.assignment.entity.Member;
import com.lguplus.assignment.entity.Post;
import com.lguplus.assignment.entity.PostView;
import com.lguplus.assignment.entity.dto.ExternalPostResponse;
import com.lguplus.assignment.entity.dto.PostDetailResponse;
import com.lguplus.assignment.entity.dto.PostRequest;
import com.lguplus.assignment.entity.dto.PostResponse;
import com.lguplus.assignment.global.exception.PostNotFoundException;
import com.lguplus.assignment.global.exception.UnauthorizedException;
import com.lguplus.assignment.global.exception.UnauthorizedPostAccessException;
import com.lguplus.assignment.global.jwt.JwtUtil;
import com.lguplus.assignment.repository.CommentRepository;
import com.lguplus.assignment.repository.MemberRepository;
import com.lguplus.assignment.repository.PostRepository;
import com.lguplus.assignment.repository.PostViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostViewRepository postViewRepository;
    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;
    private final WebClient webClient;

    @Transactional
    public PostResponse createPost(String token, PostRequest postRequest) {
        Long memberId = jwtUtil.validateToken(token);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Post post = new Post(postRequest.getTitle(), postRequest.getContent(), member, 0L, false);
        Post savedPost = postRepository.save(post);

        return new PostResponse(savedPost);
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

        return new PostResponse(updatedPost);
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

        // 댓글도 같이 삭제
        List<Comment> comments = commentRepository.findByPost(post);

        for (Comment comment : comments) {
            // 댓글 및 대댓글 소프트 삭제 처리
            comment.setDeleted();

            if (comment.getReplies() != null) {
                for (Comment reply : comment.getReplies()) {
                    softDeleteCommentAndReplies(reply);
                }
            }
        }
    }

    private void softDeleteCommentAndReplies(Comment comment) {
        // 댓글 및 대댓글 소프트 삭제 처리
        comment.setDeleted();

        if (comment.getReplies() != null) {
            for (Comment reply : comment.getReplies()) {
                softDeleteCommentAndReplies(reply);
            }
        }
    }

    // 게시글 리스트 조회
    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts(Long lastPostId, Pageable pageable) {
        Page<Post> posts = postRepository.findPostsByLastPostId(lastPostId, pageable);

        return posts.stream().map(PostResponse::new).toList();
    }

    // 동시 요청이 들어오면 어떻게 될까?
    @Transactional
    public PostDetailResponse getPostDetails(String token,Long postId) {
        Long memberId = jwtUtil.validateToken(token);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // 본인이 작성한 게시글은 조회수 증가 제외
        if (!post.getMember().getMemberId().equals(memberId)) {
            // 조회 기록이 없는 경우에만 조회수 증가
            if (!postViewRepository.existsByPostAndMemberAndViewDate(post, member, LocalDate.now())) {
                post.incrementViewCount();
                postRepository.save(post);

                // 조회 기록 저장
                PostView postView = PostView.builder()
                        .post(post)
                        .member(member)
                        .viewDate(LocalDate.now())
                        .build();

                postViewRepository.save(postView);
            }
        }
        return new PostDetailResponse(post);
    }

    public void importExternalPosts(String token) {
        // 사용자 확인
        Long memberId = jwtUtil.validateToken(token);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 외부 API 호출
        ExternalPostResponse response = webClient.get()
                .uri("/api/v1/posts")
                .retrieve()
                .bodyToMono(ExternalPostResponse.class)
                .block(); // 비동기 호출을 동기로 전환

        if (response == null || response.getPosts() == null || response.getPosts().isEmpty()) {
            throw new RuntimeException("외부 게시글 데이터가 없습니다.");
        }

        // 게시글 저장
        List<Post> posts = response.getPosts().stream()
                .map(postItem -> Post.builder()
                        .title(postItem.getSource().getTitle().trim()) // 제목
                        .content(postItem.getSource().getId().trim()) // 링크를 본문에 저장
                        .member(member)
                        .viewCount(0L)
                        .isDeleted(false)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(posts);
    }
}
