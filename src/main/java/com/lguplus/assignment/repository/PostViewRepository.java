package com.lguplus.assignment.repository;

import com.lguplus.assignment.entity.Member;
import com.lguplus.assignment.entity.Post;
import com.lguplus.assignment.entity.PostView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PostViewRepository extends JpaRepository<PostView,Long> {
    boolean existsByPostAndMemberAndViewDate(Post post, Member member, LocalDate viewDate);

    Optional<PostView> findByPostAndMember(Post post, Member member);
}
