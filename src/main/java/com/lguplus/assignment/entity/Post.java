package com.lguplus.assignment.entity;

import com.lguplus.assignment.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private Long viewCount=0L;

    public void incrementViewCount() {
        this.viewCount++;
    }

    private boolean isDeleted;

    private LocalDateTime deletedDate=null;

    @Builder
    public Post(String title, String content, Member member, Long viewCount, boolean isDeleted) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.viewCount = viewCount;
        this.isDeleted = false;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void setDeleted() {
        this.isDeleted=true;
        this.deletedDate=LocalDateTime.now();
    }
}
