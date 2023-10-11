package com.document.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.document.management.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    
}