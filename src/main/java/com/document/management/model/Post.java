package com.document.management.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;

@AllArgsConstructor
@Data
public class Post implements Serializable {
    private Long id;
    private Long documentId;
    private String userId;
    private String title;
    private String body;
}