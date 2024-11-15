package com.lguplus.assignment.repository;

import com.lguplus.assignment.entity.Post;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    // no offset 페이징
    @Query("""
    SELECT p FROM Post p 
    WHERE (:lastPostId IS NULL OR p.postId < :lastPostId) 
    AND p.isDeleted = false
    ORDER BY p.postId DESC """)
    Page<Post> findPostsByLastPostId(
            @Param("lastPostId") Long lastPostId,
            Pageable pageable);

    @EntityGraph(attributePaths = {"member"})
    @Query("SELECT p FROM Post p WHERE p.postId = :postId")
    Optional<Post> findPostDetails(@Param("postId") Long postId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Post p WHERE p.postId = :postId")
    Optional<Post> findPostWithLock(@Param("postId") Long postId);
}
