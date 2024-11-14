package com.lguplus.assignment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long memberId;

    @Column(nullable = false, unique = true, length = 50)
    private String username; // 로그인 id

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 50)
    private String nickname;

    @Column(nullable = false)
    private boolean isDeleted;

    private LocalDateTime deletedDate;

    @Builder
    public Member(String username, String password,String nickname) {
        this.username = username;
        this.password = password;
        this.nickname=nickname;
    }

    public void deleteMember(String deletionReason, Clock clock) {
        this.isDeleted = true;
        this.deletedDate = LocalDateTime.now(clock);
    }
}
