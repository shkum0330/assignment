package com.lguplus.assignment.repository;

import com.lguplus.assignment.entity.Comment;
import com.lguplus.assignment.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    void deleteByPost(Post post); // 게시글에 속한 모든 댓글 삭제
    List<Comment> findByPost(Post post);

    @Query("""
    SELECT c FROM Comment c
    WHERE c.post.postId = :postId
    AND (:lastCommentId IS NULL OR c.commentId < :lastCommentId)
    AND c.isDeleted = false
    ORDER BY c.commentId DESC
    """)
    Page<Comment> findCommentsByPostIdAndLastCommentId(
            @Param("postId") Long postId,
            @Param("lastCommentId") Long lastCommentId,
            Pageable pageable);
}
