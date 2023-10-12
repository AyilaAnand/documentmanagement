package com.document.management.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@ToString
@Data
public class Comment implements Serializable {
    private Long id;
    private Long postId;
    private String documentId;
    private String name;
    private String email;
    private String body;
}