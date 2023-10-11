package com.document.management.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.document.management.entity.Document;
import com.document.management.feign.IJsonPlaceholderFeignService;
import com.document.management.model.Comment;
import com.document.management.model.Post;
import com.document.management.repository.CommentRepository;
import com.document.management.repository.DocumentRepository;
import com.document.management.repository.PostRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentService {
    public static Logger logger = LoggerFactory.getLogger(DocumentService.class);

    @Autowired
    IJsonPlaceholderFeignService jsonPlaceholderFeignService;

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    public Document uploadDocument(MultipartFile file) {
        Document document = new Document();
        try {
            document.setFileName(file.getOriginalFilename());
            document.setFileContent(file.getBytes());
        } catch (IOException ioe) {
            logger.error("upload file transaction failed", ioe);
            throw new RuntimeException("upload file failed", ioe);
        }
        return documentRepository.save(document);
    }

    public void deleteDocument(Long documentId) {
        documentRepository.deleteById(documentId);
    }

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public void createPostOnDocument(String id, Post post) {
        jsonPlaceholderFeignService.createPostForDocument(post);
        com.document.management.entity.Post postEntity = new com.document.management.entity.Post();
        postEntity.setId(post.getId());
        postEntity.setTitle(post.getTitle());
        postEntity.setUserId(post.getUserId());
        postEntity.setBody(post.getBody());
        Document doc = new Document();
        doc.setId(Long.valueOf(id));
        postEntity.setDocument(doc);
        postRepository.save(postEntity);
    }

    public void createCommentOnDocumentForPost(String docId, String postId, Comment comment) {
        jsonPlaceholderFeignService.createCommentForDocument(comment);
        com.document.management.entity.Comment commentEntity = new com.document.management.entity.Comment();
        commentEntity.setId(comment.getId());
        commentEntity.setName(comment.getName());
        commentEntity.setEmail(comment.getEmail());
        commentEntity.setBody(comment.getBody());
        Document doc = new Document();
        doc.setId(Long.valueOf(docId));
        commentEntity.setDocument(doc);
        com.document.management.entity.Post post = new com.document.management.entity.Post();
        post.setId(Long.valueOf(postId));
        commentEntity.setPost(post);
        commentRepository.save(commentEntity);
    }

    public List<Post> getDocumentPosts(String docId) {
        List<com.document.management.entity.Post> postList = postRepository.findAllByDocumentId(docId);
        return postList.stream()
            .map(post -> new Post(post.getId(), Long.valueOf(docId), post.getUserId(), post.getTitle(), post.getBody()))
                .collect(Collectors.toList());
    }

}