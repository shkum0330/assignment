package com.lguplus.assignment.service;

import com.lguplus.assignment.entity.Member;
import com.lguplus.assignment.entity.Post;
import com.lguplus.assignment.entity.PostView;
import com.lguplus.assignment.entity.dto.response.PostDetailResponse;
import com.lguplus.assignment.global.jwt.JwtUtil;
import com.lguplus.assignment.repository.MemberRepository;
import com.lguplus.assignment.repository.PostRepository;
import com.lguplus.assignment.repository.PostViewRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class PostServiceTest {
    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostViewRepository postViewRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PersistenceContext
    private EntityManager entityManager;

    private Member writeMember,viewMember;
    private Post post;
    private String writerToken,viewerToken;

    @BeforeEach
    void setUp() {
        // 회원 생성
        writeMember = Member.builder()
                .username("writeMember")
                .password("1234")
                .nickname("작성자")
                .build();
        viewMember = Member.builder()
                .username("viewMember")
                .password("1234")
                .nickname("조회자")
                .build();
        memberRepository.save(writeMember);
        memberRepository.save(viewMember);
        // JWT 생성
        writerToken = "Bearer "+ jwtUtil.generateToken(writeMember.getMemberId());
        viewerToken = "Bearer "+ jwtUtil.generateToken(viewMember.getMemberId());
        // 게시글 생성
        post = Post.builder()
                .title("테스트 제목")
                .content("테스트 게시글입니다.")
                .member(writeMember)
                .viewCount(0L)
                .isDeleted(false)
                .build();
        postRepository.save(post);
    }

    @Test
    void 조회수가_하루에한번_증가하는지_확인() {
        // 첫 번째 조회 요청
        PostDetailResponse firstResponse = postService.getPostDetails(viewerToken, post.getPostId());
        Post updatedPost = postRepository.findById(post.getPostId()).orElseThrow();

        // 조회수 확인
        assertEquals(1L, updatedPost.getViewCount(), "첫 번째 조회 후 조회수는 1이어야 합니다.");

        // 같은 날 두 번째 조회 요청
        PostDetailResponse secondResponse = postService.getPostDetails(viewerToken, post.getPostId());
        updatedPost = postRepository.findById(post.getPostId()).orElseThrow();

        // 조회수 확인 (변하지 않아야 함)
        assertEquals(1L, updatedPost.getViewCount(), "같은 날 두 번째 조회 후 조회수는 1이어야 합니다.");

        // viewDate를 다음 날로 변경
        String updateViewDateQuery = "UPDATE post_view SET view_date = :viewDate WHERE post_id = :postId AND member_id = :memberId";
        entityManager.createNativeQuery(updateViewDateQuery)
                .setParameter("viewDate", LocalDate.now().plusDays(1))
                .setParameter("postId", post.getPostId())
                .setParameter("memberId", viewMember.getMemberId())
                .executeUpdate();

        // 다음 날 조회 요청
        PostDetailResponse thirdResponse = postService.getPostDetails(viewerToken, post.getPostId());
        updatedPost = postRepository.findById(post.getPostId()).orElseThrow();

        // 조회수 확인 (증가해야 함)
        assertEquals(2L, updatedPost.getViewCount(), "다음 날 조회 후 조회수는 2이어야 합니다.");
    }
}