package com.document.management.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@Builder
public class Post implements Serializable {
    private Long id;
    private Long documentId;
    private String userId;
    private String title;
    private String body;
}