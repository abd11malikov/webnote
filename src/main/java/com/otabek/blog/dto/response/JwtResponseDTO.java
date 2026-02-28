package com.otabek.blog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponseDTO {
    private String token;
    private String username;
}