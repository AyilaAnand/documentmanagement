package com.document.management.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.document.management.entity.Document;
import com.document.management.model.Comment;
import com.document.management.model.DocumentResponse;
import com.document.management.model.Post;
import com.document.management.service.DocumentService;

import io.micrometer.common.lang.NonNull;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/documents")
public class DocumentController {
public static Logger logger = LoggerFactory.getLogger(DocumentController.class);

    @Autowired
    private DocumentService documentService;

    @PostMapping("/upload")
    public String uploadDocument(@RequestParam("file") MultipartFile file) {
        Document document = documentService.uploadDocument(file);
        if(document.getId() > 0) {
            logger.info("document upload completed");
            return "upload successful";
        } else {
            logger.info("document upload failed");
            return "upload not successful";
        }
    }

    @DeleteMapping("/{documentId}")
    public boolean deleteDocument(@PathVariable("documentId") @NonNull String documentId) {
            return documentService.deleteDocument(Long.valueOf(documentId));
    }

    @GetMapping("/all")
    public List<DocumentResponse> getAllDocuments() {
        List<Document> documentList = documentService.getAllDocuments();
        return documentList.stream()
        .map(doc -> new DocumentResponse(doc.getId(), doc.getFileName(), 
        ServletUriComponentsBuilder.fromCurrentContextPath().path("/documents/").path(doc.getId().toString())
        .toUriString())).collect(Collectors.toList());
    }
    
    @PostMapping("/{id}/posts/create")
    public String createPostOnDocument(@PathVariable("id")  @NonNull String id, @RequestBody Post post) {
        documentService.createPostOnDocument(id, post);
        return "post created successfully"; 
    }

    @PostMapping("/{docId}/posts/{postId}/comments/create")
    public String createCommentOnDocumentForPost(@PathVariable("docId")  @NonNull String docId, @PathVariable("postId")  @NonNull String postId, @RequestBody Comment comment) {
        documentService.createCommentOnDocumentForPost(docId, postId, comment);
        return "Comment created Successfully";
    }

    @GetMapping("/{docId}/posts")
    public List<Post> getDocumentPosts(@PathVariable("docId")  @NonNull String docId) {
        return documentService.getDocumentPosts(docId);
    }
}