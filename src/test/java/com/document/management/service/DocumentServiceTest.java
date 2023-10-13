package com.document.management.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.document.management.entity.Document;
import com.document.management.feign.IJsonPlaceholderFeignService;
import com.document.management.model.Comment;
import com.document.management.model.Post;
import com.document.management.repository.CommentRepository;
import com.document.management.repository.DocumentRepository;
import com.document.management.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class DocumentServiceTest {
    
    @InjectMocks
    DocumentService documentService;

    @Mock
    private IJsonPlaceholderFeignService jsonPlaceholderFeignService;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jsonPlaceholderFeignService = mock(IJsonPlaceholderFeignService.class);
    }

    @Test
    public void testUploadDocument() {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("multipartFile", 
                                                    "test.pdf", "application/pdf", "Hello World!!!".getBytes());
        Document doc = new Document();
        doc.setFileName("test.pdf");
        doc.setFileContent("Hello World!!!".getBytes());
        when(documentRepository.save(doc)).thenReturn(doc);
        Document document = documentService.uploadDocument(mockMultipartFile);
        assertNotNull(document.getFileName());
    }

    @Test
    public void testDeleteDocument() {
        doNothing().when(documentRepository).deleteById(anyLong());
        when(documentRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertTrue(documentService.deleteDocument(anyLong()));
    }

    @Test
    public void testGetAllDocuments() {
        Document document = new Document();
        document.setId(1L);
        document.setFileName("Dummy.pdf");
        byte[] fileBytearr = new byte[1000];
        document.setFileContent(fileBytearr);
        List<Document> docList = new ArrayList<>();
        docList.add(document);
        when(documentRepository.findAll()).thenReturn(docList);
        assertTrue(documentService.getAllDocuments().size()>0);
    }

    @Test
    public void testCreatePostOnDocument() {
        Post post = new Post();
        post.setId(1L);
        post.setTitle("qui est esse");
        post.setUserId("1");
        post.setBody("est rerum tempore vitae\\n" + //
                "sequi sint nihil reprehenderit dolor beatae ea dolores neque\\n" + //
                "fugiat blanditiis voluptate porro vel nihil molestiae ut reiciendis\\n" + //
                "qui aperiam non debitis possimus qui neque nisi nulla");
        com.document.management.entity.Post postEntity = new com.document.management.entity.Post();
        postEntity.setId(1l);
        postEntity.setTitle(post.getTitle());
        postEntity.setUserId(post.getUserId());
        postEntity.setBody(post.getBody());
        Document doc = new Document();
        doc.setId(1l);
        postEntity.setDocument(doc);
        assertThrows(NullPointerException.class, () -> documentService.createPostOnDocument("1", post));
    }

    @Test
    public void testCreateCommentOnDocumentForPost() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setName("id labore ex et quam laborum");
        comment.setEmail("Eliseo@gardner.biz");
        comment.setBody("laudantium enim quasi est quidem magnam voluptate ipsam eos\\n" + //
                "tempora quo necessitatibus\\n" + //
                "dolor quam autem quasi\\n" + //
                "reiciendis et nam sapiente accusantium");
        comment.setPostId(1L);
        com.document.management.entity.Comment commentEntity = new com.document.management.entity.Comment();
        commentEntity.setName(comment.getName());
        commentEntity.setEmail(comment.getEmail());
        commentEntity.setBody(comment.getBody());
        Document doc = new Document();
        doc.setId(1l);
        commentEntity.setDocument(doc);
        com.document.management.entity.Post post = new com.document.management.entity.Post();
        post.setId(1l);
        commentEntity.setPost(post);
        assertThrows(NullPointerException.class, () -> documentService.createCommentOnDocumentForPost("1", "1", comment));
    }


   @Test
    public void testGetDocumentPosts() {
        com.document.management.entity.Post post = new com.document.management.entity.Post();
        post.setId(1L);
        post.setTitle("qui est esse");
        post.setUserId("1");
        post.setBody("est rerum tempore vitae\\n" + //
                "sequi sint nihil reprehenderit dolor beatae ea dolores neque\\n" + //
                "fugiat blanditiis voluptate porro vel nihil molestiae ut reiciendis\\n" + //
                "qui aperiam non debitis possimus qui neque nisi nulla");
        List<com.document.management.entity.Post> postList = new ArrayList<>();
        postList.add(post);
        when(postRepository.findAllByDocumentId(anyLong())).thenReturn(postList);
        assertTrue(documentService.getDocumentPosts("1").size()>0);
    }    
}
