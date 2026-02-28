package com.otabek.blog.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class PostRequestDTO {
    private String title;
    private String content;
    private Long authorId;
    private List<Long> categoryIds;
    private List<String> tags;
    private List<String> existingImageUrls;
}