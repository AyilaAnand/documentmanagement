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

import lombok.NonNull;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentService {
    public static Logger logger = LoggerFactory.getLogger(DocumentService.class);

    @Autowired
    private IJsonPlaceholderFeignService jsonPlaceholderFeignService;

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    /**
     * Upload document functionality
     * @param file
     * @return
     */
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

    /**
     * Delete the document for the given documentID
     * @param documentId
     * @return
     */
    public boolean deleteDocument(@NonNull Long documentId) {
        documentRepository.deleteById(documentId);
        if(documentRepository.findById(documentId).isEmpty()) {
            logger.info("document deleted successfully");
            return true;
        }
        logger.info("document deletion failed");
        return false;
    }

    /**
     * Fetch all documents from the system
     * @return
     */
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    /**
     * Create the post for the given documentID
     * @param id
     * @param post
     */
    public void createPostOnDocument(@NonNull String id, @NonNull Post post) {
        logger.info("post data::{}", post);
        post = jsonPlaceholderFeignService.createPostForDocument(post);
        com.document.management.entity.Post postEntity = new com.document.management.entity.Post();
        postEntity.setTitle(post.getTitle());
        postEntity.setUserId(post.getUserId());
        postEntity.setBody(post.getBody());
        Document doc = new Document();
        doc.setId(Long.valueOf(id));
        postEntity.setDocument(doc);
        postRepository.save(postEntity);
    }

    /**
     * Create the comment for the given documentID to reply for the posts
     * @param docId
     * @param postId
     * @param comment
     */
    public void createCommentOnDocumentForPost(@NonNull String docId, @NonNull String postId, @NonNull Comment comment) {
        comment = jsonPlaceholderFeignService.createCommentForDocument(comment);
        com.document.management.entity.Comment commentEntity = new com.document.management.entity.Comment();
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

    /**
     * Fetch all posts associated for the given documentID
     * @param docId
     * @return
     */
    public List<Post> getDocumentPosts(@NonNull String docId) {
        List<com.document.management.entity.Post> postList = postRepository.findAllByDocumentId(Long.valueOf(docId));
        return postList.stream()
            .map(post -> new Post(post.getId(), Long.valueOf(docId), post.getUserId(), post.getTitle(), post.getBody()))
                .collect(Collectors.toList());
    }
}