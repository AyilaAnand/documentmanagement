package com.document.management.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@ToString
@Data
public class Post implements Serializable {
    private Long id;
    private Long documentId;
    private String userId;
    private String title;
    private String body;
}