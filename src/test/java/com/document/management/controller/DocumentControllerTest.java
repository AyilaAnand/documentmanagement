package com.document.management.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.document.management.entity.Document;
import com.document.management.model.Comment;
import com.document.management.model.Post;
import com.document.management.service.DocumentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(value = DocumentController.class)
public class DocumentControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocumentService documentService;

    @BeforeEach
    public void setUp() {  }

    @Test
    public void testUploadDocument() throws Exception{
        Document document = new Document();
        document.setId(1L);
        document.setFileName("dummyFile.pdf");
        byte[] fileBytearr = new byte[1000];
        document.setFileContent(fileBytearr);
        when(documentService.uploadDocument(any())).thenReturn(document);
        MockMultipartFile mockMultipartFile = new MockMultipartFile("multipartFile", 
                                                    "test.pdf", "application/pdf", "Hello World!!!".getBytes());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/documents/upload")
                        .file("file", mockMultipartFile.getBytes())).andReturn();
        assertNotNull(result.getResponse().getContentAsString());
    }

    @Test
    public void testDeleteDocumentForTrue() throws Exception {
        String uri = "/documents/1";
        when(documentService.deleteDocument(anyLong())).thenReturn(true);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(uri).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertTrue(Boolean.parseBoolean(result.getResponse().getContentAsString()));
    }

    @Test
    public void testDeleteDocumentFalse() throws Exception {
        String uri = "/documents/1";
        when(documentService.deleteDocument(anyLong())).thenReturn(false);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(uri).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertFalse(Boolean.parseBoolean(result.getResponse().getContentAsString()));
    }

    @Test
    public void testGetAllDocuments() throws Exception {
        Document document = new Document();
        document.setId(1L);
        document.setFileName("Dummy.pdf");
        byte[] fileBytearr = new byte[1000];
        document.setFileContent(fileBytearr);
        List<Document> docList = new ArrayList<>();
        docList.add(document);
        when(documentService.getAllDocuments()).thenReturn(docList);
        String uri = "/documents/all";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertNotNull(result.getResponse().getContentAsByteArray());
    } 

    @Test
    public void testCreatePostOnDocument() throws Exception {
        Post post = new Post();
        post.setId(1L);
        post.setTitle("qui est esse");
        post.setUserId("1");
        post.setBody("est rerum tempore vitae\\n" + //
                "sequi sint nihil reprehenderit dolor beatae ea dolores neque\\n" + //
                "fugiat blanditiis voluptate porro vel nihil molestiae ut reiciendis\\n" + //
                "qui aperiam non debitis possimus qui neque nisi nulla");
        doNothing().when(documentService).createPostOnDocument(anyString(), post);
        String inputPost = new ObjectMapper().writeValueAsString(post);
        String uri = "/documents/1/posts/create";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(uri).accept(MediaType.APPLICATION_JSON)
                                        .content(inputPost);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Post respPost = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Post.class);
        assertNotNull(respPost.getId());
    } 

    @Test
    public void testCreateCommentOnDocumentForPost() throws Exception {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setName("id labore ex et quam laborum");
        comment.setEmail("Eliseo@gardner.biz");
        comment.setBody("laudantium enim quasi est quidem magnam voluptate ipsam eos\\n" + //
                "tempora quo necessitatibus\\n" + //
                "dolor quam autem quasi\\n" + //
                "reiciendis et nam sapiente accusantium");
        comment.setPostId(1L);
        String inputComment = new ObjectMapper().writeValueAsString(comment);
        doNothing().when(documentService).createCommentOnDocumentForPost(anyString(), anyString(), comment);
        String uri = "/1/posts/1/comments/create";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)
                                        .content(inputComment);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Comment respComment = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Comment.class);
        assertNotNull(respComment.getId());
    }

   @Test
    public void testGetDocumentPosts() throws Exception {
        Post post = new Post();
        post.setId(1L);
        post.setTitle("qui est esse");
        post.setUserId("1");
        post.setBody("est rerum tempore vitae\\n" + //
                "sequi sint nihil reprehenderit dolor beatae ea dolores neque\\n" + //
                "fugiat blanditiis voluptate porro vel nihil molestiae ut reiciendis\\n" + //
                "qui aperiam non debitis possimus qui neque nisi nulla");
        List<Post> postList = new ArrayList<>();
        postList.add(post);
        when(documentService.getDocumentPosts(anyString())).thenReturn(postList);
        String uri = "/documents/1/posts";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Post[] postArray = new ObjectMapper().readValue(result.getResponse().getContentAsByteArray(), Post[].class);
        assertTrue(postArray.length>0);
    }     
}