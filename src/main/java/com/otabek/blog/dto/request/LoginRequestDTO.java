package com.otabek.blog.dto.request;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String username;
    private String password;
}