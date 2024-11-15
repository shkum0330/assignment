package com.lguplus.assignment.entity;

import com.lguplus.assignment.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post; // 댓글이 속한 게시글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 댓글 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent; // 부모 댓글 (null이면 최상위 댓글)

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies; // 대댓글 리스트

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean isDeleted; // 소프트 삭제

    @Builder
    public Comment(Post post, Member member, Comment parent, String content, boolean isDeleted) {
        this.post = post;
        this.member = member;
        this.parent = parent;
        this.content = content;
        this.isDeleted = isDeleted;
    }

    public void setDeleted() {
        this.isDeleted=true;
    }
}
