package com.document.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.document.management.entity.Post;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByDocumentId(Long docId);    
}