package com.document.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.document.management.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
}