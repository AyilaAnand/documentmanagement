package com.document.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.document.management.entity.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    
}