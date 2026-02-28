package com.otabek.blog.dto.response;

import lombok.Data;
import java.util.Map;
import java.util.List;

@Data
public class UserResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String bio;
    private String profileImageUrl;
    private Map<String, String> socialMediaLinks;
    private List<PostResponseDTO> posts;
}