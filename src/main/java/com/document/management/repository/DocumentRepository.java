package com.document.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.document.management.entity.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    
}