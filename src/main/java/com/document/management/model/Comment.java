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
public class Comment implements Serializable {
    private Long id;
    private Long postId;
    private String documentId;
    private String name;
    private String email;
    private String body;
}