package com.lguplus.assignment.repository;

import com.lguplus.assignment.entity.Comment;
import com.lguplus.assignment.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByPostAndIsDeletedFalse(Post post);
    void deleteByPost(Post post); // 게시글에 속한 모든 댓글 삭제
    List<Comment> findByPost(Post post);
}
