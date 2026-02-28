package com.otabek.blog.dto.response;

import com.otabek.blog.dto.CategoryDTO;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostResponseDTO {
    private Long id;
    private String title;
    private String slug;
    private String content;
    private List<String> imageUrls;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String authorUsername;
    private List<CategoryDTO> categories;
    private List<String> tags;
}