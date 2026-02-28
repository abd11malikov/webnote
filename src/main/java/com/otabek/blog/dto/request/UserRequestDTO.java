package com.otabek.blog.dto.request;

import lombok.Data;
import java.util.Map;

@Data
public class UserRequestDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String bio;
    private Map<String, String> socialMediaLinks;
    private String profilePictureUrl;
}