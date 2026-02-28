package com.otabek.blog.controller;


import com.otabek.blog.dto.request.PostRequestDTO;
import com.otabek.blog.dto.response.PostResponseDTO;
import com.otabek.blog.service.PostService;
import com.otabek.blog.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponseDTO> createPost(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart("data") PostRequestDTO postRequestDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        Long userId = userService.getByUsername(userDetails.getUsername()).getId();
        postRequestDTO.setAuthorId(userId);
        PostResponseDTO createdPost = postService.createPost(postRequestDTO, images);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<PostResponseDTO> getPostBySlug(@PathVariable String slug) {
        PostResponseDTO postResponseDTO = postService.getPostBySlug(slug);
        return ResponseEntity.ok(postResponseDTO);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponseDTO> updatePost(
            @PathVariable Long id,
            @RequestPart("data") PostRequestDTO postRequestDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        PostResponseDTO updatedPost = postService.updatePost(id, postRequestDTO, images);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@postSecurity.isOwner(#id, authentication)")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}