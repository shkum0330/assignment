package com.lguplus.assignment.repository;

import com.lguplus.assignment.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
