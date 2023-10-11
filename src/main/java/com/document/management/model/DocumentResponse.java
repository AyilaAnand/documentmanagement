package com.document.management.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DocumentResponse {
    private Long id;
    private String fileName;
    private String fileContent;
}